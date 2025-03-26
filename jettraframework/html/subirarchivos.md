import { Jodit } from "jodit";
import "../../../node_modules/jodit/esm/plugins/justify/justify.js";
import "../components/ajax-form-handler.js";
import DataTable from "datatables.net-dt";
import language from "datatables.net-plugins/i18n/es-MX.mjs";
import * as FRT from "../crearregistrocientifico/flujo-revision-track.js";
import ModalExtensions from "../components/modal-extension.js";
import PdfViewerHandler from "../components/pdf-viewer-handler.js";

const componentId = "10";
const tipoRevisionId = "6";
document.addEventListener("DOMContentLoaded", async (event) => {
    let lastTrSelected;
    var isUpdate = false;
    if (document.querySelector(`a[data-component-id="${componentId}"]`)) {
        const me = new ModalExtensions();
        const pvh = new PdfViewerHandler(true);
        spinnerEstadoDocumentos.style.display = "block";
        await FRT.RenderUltimoEstadoRevision("divEstadoDocumentos", registroCientificoId, tipoRevisionId, UnidadRevisaId, urlGetFlujoRegistroCientificoEstado);
        spinnerEstadoDocumentos.style.display = "none";
        const editor = Jodit.make("#editorDocumento");
        const spinnerEnviarRevision = document.querySelector(`#divFormDocumento div[name="spinnerEnviarRevision"]`);

        /*
 Evento del botón Enviar
        */

        document.querySelector(`#divFormDocumento button[name="btnEnviarRevision"]`).addEventListener("click", async () => {
            if (registroCientificoDocumentosFinTable.rows().count() === 0) {
                AppGlobalMessenger.showMessage({ TypeName: "warning", Title: "Advertencia", Message: "Debe guardar al menos un documento para ser enviado a revisión" }, "alert");
                return;
            }
            let confirmResult = await AppGlobalMessenger.showConfirmDialog("Atención", "¿Está seguro que desea enviar esta revisión carta aval?");
            if (confirmResult.isConfirmed) {
                let accionRevisionId = document.querySelector(`#divFormDocumento select[name="AccionRevisionId"]`).value;
                if (Number(accionRevisionId) === 3) {
                    AppGlobalMessenger.showMessage({ TypeName: "warning", Title: "Advertencia", Message: "Acción de rechazado no disponible para opción de carta aval" }, "alert");
                    return;
                }
                const formData = new FormData();
                formData.append("RegistroCientificoId", registroCientificoId);
                formData.append("FlujoRegistroCientificoRevisarId", frcDocumentoId);
                formData.append("AccionRevisionId", accionRevisionId);
                formData.append("Observacion", document.querySelector(`#divFormDocumento textarea[name="Observacion"]`).value);
                formData.append("__RequestVerificationToken", document.querySelector(`input[name="__RequestVerificationToken"]`).value);
                event.target.disabled = true;
                spinnerEnviarRevision.style.visibility = "visible";
                fetch(urlProcesarAccionRevision, {
                    method: "POST",
                    body: formData,
                    headers: { "Accept": "application/json" }
                }).then(response => {
                    event.target.disabled = false;
                    spinnerEnviarRevision.style.visibility = "hidden";
                    return response.json();
                }).then(async data => {
                    if (data.TypeName === "success") {
                        document.querySelector(`#divFormDocumento textarea[name="Observacion"]`).value = "";
                        spinnerEstadoDocumentos.style.display = "block";
                        await FRT.RenderUltimoEstadoRevision("divEstadoDocumentos", registroCientificoId, tipoRevisionId, UnidadRevisaId, urlGetFlujoRegistroCientificoEstado);
                        spinnerEstadoDocumentos.style.display = "none";
                    }
                    AppGlobalMessenger.showMessage(data, "alert");
                }).catch(error => {
                    event.target.disabled = false;
                    spinnerEnviarRevision.style.visibility = "hidden";
                    AppGlobalMessenger.showMessage({ TypeName: "error", Title: "Error", Message: "Error al procesar la solicitud" }, "alert");
                });
            }
        });

        // Subir archivos

        $('#btnUpload').click(function () {
            var fileUpload = $("#fileUpload").get(0);
            var files = fileUpload.files;
            var fileData = new FormData();

            /**
             * Tamaño
             */
            var archivo = $('#fileUpload')[0].files[0];
            $('#errorMensaje').text("");
            // Validaciones en el cliente
            if (!archivo) {
                $('#errorMensaje').text("Por favor, selecciona un archivo.");
                return;
            }

            // Validar el tamaño máximo del archivo (2 MB)
            var tamanoMaximo = 6 * 1024 * 1024; // 4 MB en bytes
            if (archivo.size > tamanoMaximo) {
                $('#errorMensaje').text("El archivo excede el tamaño máximo permitido (6 MB).");
                return;
            }

            // Validar el tipo de archivo (PDF, JPG, PNG)
            var tiposPermitidos = ["application/pdf", "image/jpeg", "image/png", "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // Word (.docx)
                "application/vnd.ms-excel", // Excel (.xls)
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // Excel (.xlsx)
            ];
            if (!tiposPermitidos.includes(archivo.type)) {
                $('#errorMensaje').text("Tipo de archivo no permitido. Solo se aceptan PDF, JPG y PNG. DOC, DOCX, XLS, XLSX");
                return;
            }

            $('#progressBar div').width('0%');
            $('#mensaje').text('');
            /**
             * 
             */
            for (var i = 0; i < files.length; i++) {
                fileData.append(files[i].name, files[i]);
            }
            if (document.getElementById("TipoAdjuntoId").value === null) {
                AppGlobalMessenger.showMessage("Seleccione el tipo de adjunto", "alert");
                return;
            }
            fileData.append("RegistroCientificoId", registroCientificoId);
            fileData.append("TipoAdjuntoId", document.getElementById("TipoAdjuntoId").value);
            fileData.append("__RequestVerificationToken", document.querySelector(`input[name="__RequestVerificationToken"]`).value);
            $.ajax({
                url: urlUploadFilesDocumentoFin,
                type: "POST",
                contentType: false, // Not to set any content header  
                processData: false, // Not to process data  
                data: fileData,
                xhr: function () {
                    var xhr = new window.XMLHttpRequest();
                    // Escuchar el evento `progress` para actualizar la barra de progreso
                    xhr.upload.addEventListener('progress', function (evt) {
                        if (evt.lengthComputable) {
                            var percentComplete = (evt.loaded / evt.total) * 100;
                            $('#progressBar div').width(percentComplete + '%');
                            $('#progressBar div').text(Math.round(percentComplete) + '%');
                        }
                    }, false);

                    return xhr;
                },
                success: function (result) {
                    AppGlobalMessenger.showMessage(result, "alert");
                    // $('#progressBar div').hide();
                    $('#progressBar div').width('0%');
                    registroCientificoDocumentosFinTable.ajax.reload();
                },
                error: function (err) {
                    AppGlobalMessenger.showMessage({ TypeName: "error", Title: "Error", Message: "Error al procesar la solicitud" }, "alert");
                    $('#progressBar div').width('0%');
                }
            });
        });

        // Evento del botón Guardar
        document.getElementById("btnGuardarDocumentoFin").addEventListener("click", async () => {
            const formData = new FormData();
            formData.append("RegistroCientificoId", registroCientificoId);
            formData.append("UnidadProponenteId", UnidadProponenteId);
            formData.append("DocumentoFinId", document.getElementById("DocumentoFinId").value);
            formData.append("Contenido", editor.getEditorValue());
            formData.append("__RequestVerificationToken", document.querySelector(`input[name="__RequestVerificationToken"]`).value);
            event.target.disabled = true;
            spinnerEnviarRevision.style.visibility = "visible";
            fetch(urlSaveRegistroCientificoDocumentoFin, {
                method: "POST",
                body: formData,
                headers: { "Accept": "application/json" }
            }).then(response => {
                event.target.disabled = false;
                spinnerEnviarRevision.style.visibility = "hidden";
                return response.json();
            }).then(data => {
                if (data.TypeName === "success") {
                    editor.setEditorValue("");
                    registroCientificoDocumentosFinTable.ajax.reload();
                }
                AppGlobalMessenger.showMessage(data, "alert");
            }).catch(error => {
                console.error(error);
                event.target.disabled = false;
                spinnerEnviarRevision.style.visibility = "hidden";
                AppGlobalMessenger.showMessage({ TypeName: "error", Title: "Error", Message: "Error al procesar la solicitud" }, "alert");
            });
        });
        // Cargar tabla de eventos
        let registroCientificoDocumentosFinTable = new DataTable("#tableRegistroCientificoDocumentosFin", {
            language: language,
            ajax: {
                type: "GET",
                dataType: "json",
                url: `${urlGetDatosRegistroCientificoDocumentoFin}?registroCientificoId=${registroCientificoId}`,
                dataSrc: "",
                error: () => AppGlobalMessenger.showMessage({ TypeName: "error", Message: "Error al cargar los documentos" }, "sticky-notification")
            },
            columnDefs: [
                {
                    targets: [0,1, 2,3],
                    visible: false
                },
                {
                    targets: "_all",
                    render: DataTable.render.text()
                }
            ],
            columns: [
                { data: "Id" },
                { data: "RegistroCientificoId" },
                { data: "DocumentoFinId" },
                { data: "RutaRelativa" },
                { data: "Tipo" },
                {
                    data: "Nombre",
                },
                { data: "Fecha" },
                {
                    data: "Archivo",
                    render: (data, type, row) => {
                        if (data === "NO") {
                            return `<button type="button" name="btnRegistroCientificoDocumentoFin" class="btn btn-success">
                                      <i class="fa-solid fa-circle-arrow-up" style="pointer-events:none"></i>
                                    </button>`;
                        }
                        else {
                            return `<button type="button" name="btnRegistroCientificoTipoArchivoDelete" class="btn btn-danger">
                            <i class="fa-solid fa-trash" style="pointer-events:none"></i>
                          </button>`;
                        }
                    }
                },
                {
                    data: "Archivo",
                    render: (data, type, row) => {
                        if (data === "NO") {
                            return `<button type="button" name="btnImprimirDocumentoFin" class="btn btn-warning">
                <i class="fa-solid fa-print" style="pointer-events:none"></i>
              </button>`;
                        }
                        else {
                            return `<button type="button" onclick="window.open('${row.RutaRelativa}', '_blank')"  class="btn btn-primary">
                <i class="fa-solid fa-down-long" style="pointer-events:none"></i>
              </button>`;
                        }
                    }
                }
            ]
        });
        //---------------------------------------------------
        // Evento del clic en la fila de la tabla tableRegistroCientificoDocumentosFin
        //---------------------------------------------------
        document.querySelector("#tableRegistroCientificoDocumentosFin tbody").addEventListener("click", async (event) => {
            if (registroCientificoDocumentosFinTable.rows().count() > 0 && event.target.tagName === "BUTTON") {
                let trElement = event.target.parentElement.parentElement;
                /**
                 * Eliminar archivo
                 */

                if (event.target.getAttribute("name") === "btnRegistroCientificoTipoArchivoDelete") {
                    let confirmResult = await AppGlobalMessenger.showConfirmDialog("Atención", "¿Está seguro que desea borrar el Archivo "+registroCientificoDocumentosFinTable.row(trElement).data().Nombre + "");
                    if (confirmResult.isConfirmed) {
                        const formData = new FormData();
                        formData.append("RegistroCientificoId", registroCientificoId);
                        formData.append("Id", registroCientificoDocumentosFinTable.row(trElement).data().Id);
                        formData.append("TipoAdjuntoId", registroCientificoDocumentosFinTable.row(trElement).data().DocumentoFinId);
                        formData.append("Ruta", registroCientificoDocumentosFinTable.row(trElement).data().RutaRelativa);
                        formData.append("__RequestVerificationToken", document.querySelector(`input[name="__RequestVerificationToken"]`).value);
                        event.target.disabled = true;
                        spinnerEstadoDocumentos.style.display = "block";
                        fetch(urlDeleteRegistroCientificoTipoArchivo, {
                            method: "POST",
                            body: formData,
                            headers: { "Accept": "application/json" }
                        }).then(response => {
                            event.target.disabled = false;
                            spinnerEstadoDocumentos.style.display = "none";
                            return response.json();
                        }).then(data => {
                            AppGlobalMessenger.showMessage(data, "alert");
                            if (data.TypeName === "success") {
                                // RestartFormRequerimiento(sltCategoriaRequerimientoId, sltRequerimientoId, fe);
                                // LoadRequerimientosTable(datosProyectoRC.RegistroCientificoId);
                                // data.ActualizarRevision === true && btnActualizarRevision.click();
                                registroCientificoDocumentosFinTable.ajax.reload();
                            }
                        }).catch(error => {
                            event.target.disabled = false;
                            spinnerEstadoDocumentos.style.display = "none";
                            AppGlobalMessenger.showMessage({ TypeName: "error", Title: "Error", Message: "Error al procesar la solicitud" }, "alert");
                        });
                    }
                }

                /* 
                Seleccionar un documento de la tabla
                 */
                if (event.target.getAttribute("name") === "btnRegistroCientificoDocumentoFin") {
                    if (trElement.classList.contains("selected")) {
                        isUpdate = false;
                        trElement.classList.remove("selected");
                        editor.setEditorValue("");
                    }
                    else {
                        isUpdate = true;
                        lastTrSelected && lastTrSelected.classList.remove("selected");
                        trElement.classList.add("selected");
                        lastTrSelected = trElement;
                        let objData = registroCientificoDocumentosFinTable.row(trElement).data();
                        document.getElementById("DocumentoFinId").value = objData.DocumentoFinId;
                        editor.setEditorValue(objData.Contenido);
                        document.querySelector(`#divFormDocumento textarea[name="Observacion"]`).value = "";
                    }
                }
                else {
                    // Imprimir el documento
                    if (event.target.getAttribute("name") === "btnImprimirDocumentoFin") {
                        let objData = registroCientificoDocumentosFinTable.row(trElement).data();
                        pvh.LoadReportOnModal(`${urlPrintRegistroCientificoDocumentoFin}?registroCientificoId=${registroCientificoId}&documentoFinId=${objData.DocumentoFinId}`).then(modal => { });
                    }
                }
            }
        });

        // Evento del botón Eliminar documento
        document.getElementById("btnEliminarDocumentoFin").addEventListener("click", async () => {
            const formData = new FormData();
            if (isUpdate === false) {
                AppGlobalMessenger.showMessage({ TypeName: "warning", Message: "Debe seleccionar un Documento para borrar" }, "sticky-notification");
                return;
            }
            else {
                let confirm = await AppGlobalMessenger.showConfirmDialog("Cuidado", "¿Está seguro que desea Borrar el documento?");
                if (confirm.isConfirmed === true) { // Aqui va el codigo para enviar 
                    formData.append("RegistroCientificoId", registroCientificoId);
                    formData.append("UnidadProponenteId", UnidadProponenteId);
                    formData.append("DocumentoFinId", document.getElementById("DocumentoFinId").value);
                    formData.append("Contenido", editor.getEditorValue());
                    formData.append("__RequestVerificationToken", document.querySelector(`input[name="__RequestVerificationToken"]`).value);
                    event.target.disabled = true;
                    spinnerEnviarRevision.style.visibility = "visible";
                    fetch(urlDeleteRegistroCientificoDocumentoFin, {
                        method: "POST",
                        body: formData,
                        headers: { "Accept": "application/json" }
                    }).then(response => {
                        //event.target.disabled = false;
                        spinnerEnviarRevision.style.visibility = "hidden";
                        return response.json();
                    }).then(data => {
                        if (data.TypeName === "success") {
                            editor.setEditorValue("");
                            registroCientificoDocumentosFinTable.ajax.reload();
                        }
                        AppGlobalMessenger.showMessage(data, "alert");
                    }).catch(error => {
                        console.error(error);
                        event.target.disabled = false;
                        spinnerEnviarRevision.style.visibility = "hidden";
                        AppGlobalMessenger.showMessage({ TypeName: "error", Title: "Error", Message: "Error al procesar la solicitud" }, "alert");
                    });
                }
            }
        });
        //Evento de Historial de Revision
        document.querySelector(`#divEstadoDocumentos`).addEventListener("click", async (event) => {
            if (event.target.tagName === "BUTTON" && event.target.getAttribute("name") === "btnVerHistorialFRC") {
                let unidadPerteneceId = event.target.getAttribute("data-unidad-pertenece-id");
                me.SetStatusModalBody("modalVerHistorialFRC", "spinnerBodyVerHistorialFRC", false);
                window.verHistorialFRCModal.show();
                await FRT.LoadFlujoRevisionHistorialTable("divTableVerHistorialFRC", registroCientificoId, tipoRevisionId, unidadPerteneceId, urlGetFlujoRevisionHistorial);
                me.SetStatusModalBody("modalVerHistorialFRC", "spinnerBodyVerHistorialFRC", true);
            }
        });
        // Evento de reporte
        document.getElementById("btnDatosProyectoRectoriaRpt").addEventListener("click", (event) => {
            pvh.LoadReportOnModal(`${urlDatosProyectoRpt}&registroCientificoId=${registroCientificoId}`).then(modal => { });
        });
    }
});

