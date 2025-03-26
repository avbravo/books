/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sft.faces;

import com.avbravo.jmoordbutils.ConsoleUtil;
import com.avbravo.jmoordbutils.DateUtil;
import com.avbravo.jmoordbutils.FacesUtil;
import com.avbravo.jmoordbutils.JmoordbCoreContext;
import com.avbravo.jmoordbutils.JmoordbCoreDateUtil;
import com.avbravo.jmoordbutils.JmoordbCoreXHTMLUtil;
import com.avbravo.jmoordbutils.JmoordbResourcesFiles;
import com.avbravo.jmoordbutils.media.JmoordbCoreMediaContext;
import com.avbravo.jmoordbutils.media.JmoordbCoreMediaManager;
import com.avbravo.jmoordbutils.paginator.IPaginator;
import com.avbravo.jmoordbutils.paginator.Paginator;
import com.jmoordb.core.model.Pagination;
import com.jmoordb.core.model.Sorted;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import com.sft.client.ProyectoClient;
import com.sft.client.UserClient;
import com.sft.model.ActionHistory;
import com.sft.model.EstadisticaCierre;
import com.sft.services.implementation.ColorManagementImpl;
import com.sft.model.Profile;
import com.sft.model.Proyecto;
import com.sft.model.Sprint;
import com.sft.model.Tarjeta;
import com.sft.model.User;
import com.sft.services.ProyectoServices;
import com.sft.services.ProyectoViewServices;
import com.sft.services.SprintServices;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.model.SortMeta;
import com.sft.services.ColorManagement;
import com.sft.services.TarjetaServices;
import java.util.Optional;

/**
 *
 * @author avbravo
 */
@Named
@ViewScoped
@Data
public class SprintFaces implements Serializable, JmoordbCoreXHTMLUtil, IPaginator {

    // <editor-fold defaultstate="collapsed" desc=" fields">
    private static final long serialVersionUID = 1L;
    private User userLogged = new User();
    private String message = "";

    private Profile profileLogged = new Profile();

    private Sprint sprintSelected = new Sprint();
    private Proyecto proyectoSelected = new Proyecto();

    private List<ResponsiveOption> responsiveOptions;

    private List<Sprint> sprintList = new ArrayList<>();
    private List<Tarjeta> tarjetaList = new ArrayList<>();

    private Boolean haveSprintOpen = Boolean.FALSE;
    private Boolean isRowPageSmall = Boolean.TRUE;

    ColorManagement colorKnob = new ColorManagementImpl();
    private DataTable dataTable;
    Integer totalRecords = 0;

    private List<Tarjeta> tarjetaPendienteList = new ArrayList<>();

    private List<Tarjeta> tarjetaProgresoList = new ArrayList<>();

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="LazyDataModel">
    private LazyDataModel<Sprint> sprintLazyDataModel;
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="paginator ">
    Paginator paginator = new Paginator();
    Paginator paginatorOld = new Paginator();

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="@Inject">
    @Inject
    JmoordbResourcesFiles rf;
    @Inject
    JmoordbCoreMediaManager jmoordbCoreMediaManager;
    @Inject
    JmoordbCoreMediaContext jmoordbCoreMediaContext;

// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Services">
    @Inject
    ProyectoServices proyectoServices;

    @Inject
    ProyectoViewServices proyectoViewServices;
    @Inject
    SprintServices sprintServices;

    @Inject
    TarjetaServices tarjetaServices;
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Microprofile Config">
    @Inject
    private Config config;
    @Inject
    @ConfigProperty(name = "idapplicative")
    private Provider<Integer> idapplicative;
    @Inject
    @ConfigProperty(name = "loginStyle")
    private Provider<String> loginStyle;
    @Inject
    @ConfigProperty(name = "application.version")
    private Provider<String> applicationVersion;
    @Inject
    @ConfigProperty(name = "smallSizeOfTextForCut")
    private Provider<Integer> smallSizeOfTextForCut;
    @Inject
    @ConfigProperty(name = "mediumSizeOfTextForCut")
    private Provider<Integer> mediumSizeOfTextForCut;
    @Inject
    @ConfigProperty(name = "largeSizeOfTextForCut")
    private Provider<Integer> largeSizeOfTextForCut;

    // Row
    @Inject
    @ConfigProperty(name = "rowPage", defaultValue = "0")
    private Provider<Integer> rowPage;
    @Inject
    @ConfigProperty(name = "rowPageSmall", defaultValue = "0")
    private Provider<Integer> rowPageSmall;

    // </editor-fold>
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Microprofile Rest Client">
    @Inject
    ProyectoClient proyectoClient;

    @Inject
    UserClient userClient;
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" init">
    @PostConstruct
    public void init() {
        try {
            haveSprintOpen = Boolean.FALSE;
            message = "";

            userLogged = (User) JmoordbCoreContext.get("LoginFaces.userLogged");
            profileLogged = (Profile) JmoordbCoreContext.get("LoginFaces.profileLogged");
            if (JmoordbCoreContext.get("DashboardFaces.proyectoSelected") == null) {

            } else {
                proyectoSelected = (Proyecto) JmoordbCoreContext.get("DashboardFaces.proyectoSelected");
            }
            sprintSelected.setSprint("sp-" + sprintServices.generateNumberForSprint(proyectoSelected));
            sprintSelected.setDescripcion("Objetivo del Sprint:");
            sprintSelected.setFechainicial(JmoordbCoreDateUtil.fechaHoraActual());

            sprintSelected.setFechafinal(JmoordbCoreDateUtil.sumarDiaaFecha(JmoordbCoreDateUtil.fechaHoraActual(), 5));
            sprintSelected.setActive(Boolean.TRUE);
            sprintSelected.setOpen(Boolean.TRUE);
            sprintSelected.setProyectoView(proyectoViewServices.convertFromProyecto(proyectoSelected));

            saveToMediaContext(userLogged.getPhoto());
            responsiveOptions = new ArrayList<>();
            responsiveOptions.add(new ResponsiveOption("1024px", 3, 3));
            responsiveOptions.add(new ResponsiveOption("768px", 2, 2));
            responsiveOptions.add(new ResponsiveOption("560px", 1, 1));

            findByIdProyecto();

            this.sprintLazyDataModel = new LazyDataModel<Sprint>() {
                @Override
                public List<Sprint> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {

                    switch (paginator.getName()) {
                        case "findByIdProyecto":

                            totalRecords = sprintServices.count(paginator.getFilter(),
                                    paginator.getSort(), 0, 0).intValue();
                            break;

                    }

                    List<Paginator> list = new ArrayList<>();
                    if (!isRowPageSmall) {
                        /**
                         * Utiliza rowPage
                         */
                        list = processLazyDataModel(paginator, paginatorOld, offset, rowPage.get(), totalRecords, sortBy);

                    } else {
                        /**
                         * Utiliza rowPageWithOverlayPanel para el OverlayPanel
                         */
                        list = processLazyDataModel(paginator, paginatorOld, offset, rowPageSmall.get(), totalRecords, sortBy);

                    }
//

                    paginator = list.get(0);
                    paginatorOld = list.get(1);
                    Pagination pagination = new Pagination();
                    if (!isRowPageSmall) {
                        paginator.setNumberOfPage(numberOfPages(totalRecords, rowPage.get()));
                        pagination = new Pagination(paginator.getPage(), rowPage.get());
                    } else {
                        paginator.setNumberOfPage(numberOfPages(totalRecords, rowPageSmall.get()));
                        pagination = new Pagination(paginator.getPage(), rowPageSmall.get());
                    }

                    List<Sprint> result = new ArrayList<>();
                    switch ((paginator.getName())) {
                        case "findByIdProyecto":

                            result = sprintServices.lookup(paginator.getFilter(),
                                    paginator.getSort(), paginator.getPage(), rowPageSmall.get());
                            break;
                        default:

                    }

                    sprintLazyDataModel.setRowCount(totalRecords);

                    PrimeFaces.current().executeScript("setDataTableWithPageStart()");

                    return result;
                }

                @Override
                public int count(Map<String, FilterMeta> map) {

                    return totalRecords;
                }

            };

        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String findByIdProyecto()">
    public String findByIdProyecto() {
        try {

            Bson filter = and(eq("proyecto.idproyecto", proyectoSelected.getIdproyecto()),
                    eq("active", Boolean.TRUE)
            );
            Document sort = new Document("idsprint", -1);

            paginator
                    = new Paginator.Builder()
                            .page(1)
                            .filter(filter)
                            .sort(sort)
                            .sorted(new Sorted(new Document("idsprint", -1)))
                            .title("Todos")
                            .name("findByIdProyecto")
                            .build();

            /**
             * Limpiar los elementos
             */
            setFirstPageDataTable();
        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfMethod() + "() : " + e.getLocalizedMessage());
        }

        return "";
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String saveToMediaContext(String pathOfFile)">
    public String saveToMediaContext(String pathOfFile) {
        try {

            jmoordbCoreMediaContext.put("pathOfFile", pathOfFile);
        } catch (Exception e) {

        }

        return "";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setFirstPageDataTable()">
    public void setFirstPageDataTable() {

        dataTable.setFirst(1);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String go(Sprint sprint)">
    public String go(Sprint sprint) {
        try {
            sprintSelected = sprint;

            JmoordbCoreContext.put("SprintFaces.sprintSelected", sprintSelected);
        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return "tablero.xhtml";
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String closeSprint() ">
    public String closeSprint() {
        try {
            sprintSelected.setSprint(sprintSelected.getSprint().trim());

            if (haveOpenSprint()) {
                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.tienesprintabiertosnosepuedencrearnuevoshastacerrarlos"));
                return "";
            }

            if (DateUtil.fechaMayor(sprintSelected.getFechainicial(), sprintSelected.getFechafinal())) {
                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.fechainicialmayorfechafinal"));
                return "";
            }
            if (!DateUtil.dateBetweenExcludeTime(sprintSelected.getFechainicial(), proyectoSelected.getFechainicial(), proyectoSelected.getFechafinal())) {

                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.fechainicialnoestarangofechasproyecto"));
                return "";
            }

            if (!DateUtil.dateBetweenExcludeTime(sprintSelected.getFechafinal(), proyectoSelected.getFechainicial(), proyectoSelected.getFechafinal())) {

                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.fechafinalnoestarangofechasproyecto"));
                return "";
            }

            if (sprintServices.existsBySprintAndProject(proyectoSelected, sprintSelected)) {
                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.exitsotherdocumentwiththisname"));
                return "";

            }
            /**
             *
             */
            if (sprintServices.exitsBetweenDates(proyectoSelected, sprintSelected.getFechainicial(), sprintSelected.getFechafinal())) {
                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.existesprintenesasfechas"));
                return "";

            }

            sprintSelected.setSprint(sprintSelected.getSprint().trim());
            if (!sprintServices.save(sprintSelected).isPresent()) {
                FacesUtil.warningDialog(rf.fromCore("warning.save"), rf.fromCore("warning.save"));
            } else {
                JmoordbCoreContext.put("SprintFaces.sprintSelected", sprintSelected);
                FacesUtil.infoDialog(rf.fromCore("info.save"), rf.fromCore("info.save"));

                return "tablero.xhtml";
            }

        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return "";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean haveOpenSprint()">
    /**
     * Muesta el commandButton save solo si no hay Sprint abiertos.
     *
     * @return
     */
    public Boolean haveOpenSprint() {
        Boolean result = Boolean.FALSE;
        try {
            if (sprintServices.haveOpenSprint(proyectoSelected)) {
                result = Boolean.TRUE;
            }
        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return result;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String save(Sprint sprint)">
    public String save() {
        try {
            sprintSelected.setSprint(sprintSelected.getSprint().trim());

            if (haveOpenSprint()) {
                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.tienesprintabiertosnosepuedencrearnuevoshastacerrarlos"));
                return "";
            }

            if (DateUtil.fechaMayor(sprintSelected.getFechainicial(), sprintSelected.getFechafinal())) {
                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.fechainicialmayorfechafinal"));
                return "";
            }
            if (!DateUtil.dateBetweenExcludeTime(sprintSelected.getFechainicial(), proyectoSelected.getFechainicial(), proyectoSelected.getFechafinal())) {

                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.fechainicialnoestarangofechasproyecto"));
                return "";
            }

            if (!DateUtil.dateBetweenExcludeTime(sprintSelected.getFechafinal(), proyectoSelected.getFechainicial(), proyectoSelected.getFechafinal())) {

                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.fechafinalnoestarangofechasproyecto"));
                return "";
            }

            if (sprintServices.existsBySprintAndProject(proyectoSelected, sprintSelected)) {
                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.exitsotherdocumentwiththisname"));
                return "";

            }
            /**
             *
             */
            if (sprintServices.exitsBetweenDates(proyectoSelected, sprintSelected.getFechainicial(), sprintSelected.getFechafinal())) {
                FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromCore("warning.existesprintenesasfechas"));
                return "";

            }

            sprintSelected.setSprint(sprintSelected.getSprint().trim());

            sprintSelected.setEstadisticaCierre(new EstadisticaCierre());
  ActionHistory actionHistory = new ActionHistory.Builder()
                    .iduser(userLogged.getIduser())
                    .fecha(JmoordbCoreDateUtil.fechaHoraActual())
                    .evento("crear")
                    .clase(FacesUtil.nameOfClass())
                    .metodo(FacesUtil.nameOfMethod())
                    .build();
            List<ActionHistory> actionHistoryList = new ArrayList<>();
            actionHistoryList.add(actionHistory);

           sprintSelected.setActionHistory(actionHistoryList);
            
            Optional<Sprint> sprintOptional = sprintServices.save(sprintSelected);
            if (!sprintOptional.isPresent()) {
                FacesUtil.warningDialog(rf.fromCore("warning.save"), rf.fromCore("warning.save"));
            } else {
                sprintSelected = sprintOptional.get();
                JmoordbCoreContext.put("SprintFaces.sprintSelected", sprintSelected);
                FacesUtil.infoDialog(rf.fromCore("info.save"), rf.fromCore("info.save"));
                
                sacarTarjetasDelBacklog();
               
                /**
                 * Actualiza las tarjetas
                 */
                return "tablero.xhtml";
            }

        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return "";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String prepareCloseSprint()">
    public String prepareCloseSprint() {
        try {
            JmoordbCoreContext.put("SprintFaces.sprintSelected", sprintSelected);
        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return "sprintclose.xhtml";
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean loadTarjetaPendienteProgreso()">
    private Boolean sacarTarjetasDelBacklog() {
        Boolean result = Boolean.FALSE;
        tarjetaPendienteList = new ArrayList<>();
        try {
            Integer page = 0;
            Integer size = 0;
            Document sortTarjeta = new Document("idtarjeta", 1).append("prioridad", 1);
            /**
             * CargarTarjetas
             */
            Bson filter0 = eq("idproyecto", proyectoSelected.getIdproyecto());
            Bson filter = and(filter0, eq("active", Boolean.TRUE),
                    eq("columna", "pendiente"));

            tarjetaPendienteList = tarjetaServices.lookup(filter, sortTarjeta, page, size);
            
            Bson filterProgreso = and(filter0,  eq("active", Boolean.TRUE),
                    eq("columna", "progreso"));

            tarjetaProgresoList = tarjetaServices.lookup(filter, sortTarjeta, page, size);
            
            
            actualizarTarjeta(tarjetaPendienteList);
            actualizarTarjeta(tarjetaProgresoList);
            

        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="void actualizarTarjeta(List<Tarjeta> list">


    public void actualizarTarjeta(List<Tarjeta> list){
        try{
               if(list == null || list.isEmpty()){
                
            }else{
                for(Tarjeta t:list){
                    t.setBacklog(Boolean.FALSE);
                    t.setIdsprint(sprintSelected.getIdsprint());
                    
                     ActionHistory actionHistory = new ActionHistory.Builder()
                    .iduser(userLogged.getIduser())
                    .fecha(JmoordbCoreDateUtil.fechaHoraActual())
                    .evento("editar")
                    .clase(FacesUtil.nameOfClass())
                    .metodo(FacesUtil.nameOfMethod())
                    .build();

            t.getActionHistory().add(actionHistory);

                    
                    tarjetaServices.update(t);
                }
            }
         } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
    }
// </editor-fold>
}
