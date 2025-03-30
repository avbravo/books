inplace
```html

                    <p:inplace  editor="true" >

                        <p:ajax event="save" listener="#{tableroFaces.updateNombreTarjetaInline(tarjeta)}"
                                process="@this" 
                                update=":form:growl,@form"
                                />
                   

                        <f:facet name="output">
                            <p:outputLabel value="#{tarjeta.tarjeta}"/>
                        </f:facet>
                        <f:facet name="input">
                            <p:outputLabel value="#{tarjeta.tarjeta}" rendered="!#{tableroFaces.tienePrivilegiosParaTarjeta(tarjeta)}"/>
                            <p:inputText value="#{tarjeta.tarjeta}" required="true" label="text"
                                         rendered="#{tableroFaces.tienePrivilegiosParaTarjeta(tarjeta)}"
                                         onkeypress="if (event.keyCode == 13) {
                                                     return false;
                                                 }"
                                         title="#{msg['title.tarjetaclickeditar']}"
                                         requiredMessage="#{msg['required.nombretarjeta']}"

                                         >
                            </p:inputText>
                        </f:facet>

                    </p:inplace>

``


```java

 // <editor-fold defaultstate="collapsed" desc="updateNombreTarjetaInline(Tarjeta tarjeta)">
    public void updateNombreTarjetaInline(Tarjeta tarjeta) {

        try {

            if (tarjeta.getTarjeta() == null || tarjeta.getTarjeta().equals("")) {
                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromMessage("warning.nombretarjeta"));
                return;
            }
            validarSiCambioEnPrepare(tarjeta);

            /**
             * Comprueba si la tarjeta fue movida por otro usuario
             */
            if (!tarjeta.getColumna().equals(tarjetaDB.getColumna())) {

                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromMessage("warning.otrousuarioactualizotarjetasincronizeeltablero"));
                refresh();
                return;
            }

            /**
             * Busca la tarjeta en la lista local
             */
            Boolean found = Boolean.FALSE;
            tarjetaInlineSelected = new Tarjeta();

            switch (tarjetaDB.getColumna()) {
                case "pendiente":
                    for (Tarjeta t : tarjetaPendienteInitialList) {
                        if (t.getIdtarjeta().equals(tarjeta.getIdtarjeta())) {
                            found = Boolean.TRUE;
                            tarjetaInlineSelected = t;
                            break;
                        }
                    }
                    break;
                case "progreso":
                    for (Tarjeta t : tarjetaProgresoInitialList) {
                        if (t.getIdtarjeta().equals(tarjeta.getIdtarjeta())) {
                            found = Boolean.TRUE;
                            tarjetaInlineSelected = t;
                            break;
                        }
                    }
                    break;
                case "finalizado":
                    for (Tarjeta t : tarjetaFinalizadoInitialList) {
                        if (t.getIdtarjeta().equals(tarjeta.getIdtarjeta())) {
                            found = Boolean.TRUE;
                            tarjetaInlineSelected = t;
                            break;
                        }
                    }
                    break;
            }
            if (!found) {

                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromMessage("warning.tarjetafueremovidaporotrousuario"));
                return;
            }

            if (!tarjetaInlineSelected.getTarjeta().equals(tarjetaDB.getTarjeta())) {

                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromMessage("warning.otrousuarioactualizotarjetasincronizeeltablero"));
                refresh();
                return;

            }
//           

            ActionHistory actionHistory = new ActionHistory.Builder()
                    .iduser(userLogged.getIduser())
                    .fecha(JmoordbCoreDateUtil.fechaHoraActual())
                    .evento("editar nombre")
                    .clase(FacesUtil.nameOfClass())
                    .metodo(FacesUtil.nameOfMethod())
                    .build();

            tarjeta.getActionHistory().add(actionHistory);

            if (!tarjetaServices.update(tarjeta)) {

                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.update"));

            } else {
                isOverlayPanelOpen = Boolean.FALSE;
                isButtonSavePressed = Boolean.FALSE;
                FacesUtil.successMessage(rf.fromMessage("info.updatetarjeta"));
                sendEmailTarjeta(tarjeta, "editar");

                //  closeOverlayPanel("PF('overlayPanelTarjetaEditar').hide()");
                refresh();
            }

        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

    }

// </editor-fold>
```
