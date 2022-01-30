/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GESRE.controller;

import GESRE.entidades.Trabajador;
import static GESRE.entidades.UserPrivilege.TRABAJADOR;
import static GESRE.entidades.UserStatus.ENABLED;
import GESRE.excepcion.EmailExisteException;
import GESRE.excepcion.LoginExisteException;
import GESRE.factoria.GestionFactoria;
import GESRE.interfaces.TrabajadorManager;
import GESRE.interfaces.UsuarioManager;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Jonathan Viñan
 */
public class GestionTrabajadorViewController {

    /**
     * Atributo estático y constante que guarda los loggers de la clase.
     */
    public static final Logger LOGGER = Logger.getLogger("controladores.GestionTrabajadorController");
    /**
     * Atributo estático y constante que guarda los caracteres máximos admitidos
     * en los campos de texto.
     */
    private static final int MAX_LENGHT = 50;
    /**
     * Atributo estático y constante que guarda los caracteres máximos admitidos
     * en los campos de nombre de usuario.
     */
    private static final int MAX_LENGHT_USER = 25;
    /**
     * Atributo estático y constante que guarda los caracteres máximos admitidos
     * en los campos de precio/hora.
     */
    private static final float MAX_LENGHT_CANTIDAD = Float.max(2, 2);
    /**
     * Atributo estático y constante que guarda el patron correcto del nombre
     * completo.
     */
    public static final Pattern VALID_NOMBRE_COMPLETO = Pattern.compile("^[A-Z\\s]+$", Pattern.CASE_INSENSITIVE);
    /**
     * Atributo estático y constante que guarda el patron correcto del email.
     */
    public static final Pattern VALID_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Atributo estático y constante que guarda el patron correcto del usuario.
     */
    public static final Pattern VALID_USUARIO = Pattern.compile("^[A-Z0-9]+$", Pattern.CASE_INSENSITIVE);
    /**
     * Atributo estático y constante que guarda el patron correcto de isbn y
     * cantidad total.
     */
    public static final Pattern VALID_NUMERO = Pattern.compile("^[0-9]", Pattern.CASE_INSENSITIVE);

    //********PANE********
    /**
     * Elemento tipo pane importado de la vista FXML.
     */
    @FXML
    private Pane paneGeneralTrabajador;
    @FXML
    private Pane fxPane;

    //********BARRA MENU********
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem mnCerrarSecion;
    @FXML
    private MenuItem mnSalir;

    //********LABEL********
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblNombreCompleto;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblPrecioHora;
    @FXML
    private Label lblFechaContrato;
    @FXML
    private Label lblNombreUsuario;
    @FXML
    private Label lblContrasenia;
    @FXML
    private Label lblRepiteContrasenia;

    //********LABEL DE ERROR********
    @FXML
    private Label lblErrorNombreCompleto;
    @FXML
    private Label lblErrorEmail;
    @FXML
    private Label lblErrorPrecioHora;
    @FXML
    private Label lblErrorFechaContrato;
    @FXML
    private Label lblErrorNombreUsuario;
    @FXML
    private Label lblErrorContrasenia;
    @FXML
    private Label lblErrorRepiteContrasenia;
    @FXML
    private Label lblErrorBuscar;

    //********TEXT FIEL********
    @FXML
    private TextField txtNombreCompleto;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPrecioHora;

    @FXML
    private TextField txtNombreUsuario;
    @FXML
    private TextField txtBuscar;

    //********PASSWORD FIEL********
    @FXML
    private PasswordField txtContrasenia;
    @FXML
    private PasswordField txtRepiteContrasenia;

    //********DATE PICKER********
    @FXML
    private DatePicker datePikerFechaContrato;

    //********COMBOBOX********
    @FXML
    private ComboBox<String> cbxFiltro;

    //********BUTTON********
    @FXML
    private Button btnAnadir;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnInforme;
    @FXML
    private Button btnGestionarClientes;

    //********TABLE********
    @FXML
    private TableView<Trabajador> tablaTrabajadores;

    //********TABLE COLUM********
    @FXML
    private TableColumn<Trabajador, String> nombreUsuarioCL;
    @FXML
    private TableColumn<Trabajador, String> nombreCompletoCL;
    @FXML
    private TableColumn<Trabajador, Date> fechaContratoCL;
    @FXML
    private TableColumn<Trabajador, String> PrecioHoraCL;

    //*********ENTIDADA GESTION********
    /**
     * Variable que hace una llamada al método que gestiona los grupos de la
     * factoría.
     */
    TrabajadorManager trabajadorGestion = GestionFactoria.getTrabajadorGestion();
    /**
     * Variable que hace una llamada al método que gestiona los grupos de la
     * factoría.
     */
    UsuarioManager usuarioManager = GestionFactoria.getUsuarioGestion();

    /**
     * Variable de tipo stage que se usa para visualizar la ventana.
     */
    private Stage stage;

    /**
     * Método que establece el escenario como escenario principal.
     *
     * @param primaryStage El escenario de la view Trabajador.
     */
    public void setStage(Stage primaryStage) {
        LOGGER.info("Trabajador Controlador: Estableciendo stage");
        this.stage = primaryStage;
    }

    /**
     * Método que inicializa el escenario y los componentes de la vista.
     *
     * @param root El objeto padre que representa el nodo root.
     */
    public void initStage(Parent root) {
        try {
            LOGGER.info("Trabajador Controlador: Iniciando stage");

            //Iniciando ventana
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestion de trabajadores");
            stage.setResizable(false);
            //Accion cuando se Pulsa X parte arriba a la derrecha del programa
            stage.onCloseRequestProperty().set(this::cerrarVentana);
            // Estado inicial de los controles
            btnAnadir.setDisable(true);
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
            btnLimpiar.setDisable(true);
            btnBuscar.setDisable(true);
            btnLimpiar.setDisable(true);
            txtNombreCompleto.requestFocus();
            txtBuscar.setDisable(true);

            //Gargar filtros de los casos de usos.
            cargarFiltros();
            // Añade listeners a propiedades de cambio de texto
            textoNuevo();

            //Añadir la seleccion de la tabla a los campos de texto
            tablaTrabajadores.getSelectionModel().selectedItemProperty().addListener(this::handleTablaSeleccionada);

            //DatePicker Fecha contrato
            datePikerFechaContrato.setValue(LocalDate.now());
            noSeleccionarFechaAnterior(datePikerFechaContrato);

            //Definir columnas de la tabla trabajador
            nombreUsuarioCL.setCellValueFactory(new PropertyValueFactory<>("login"));
            nombreCompletoCL.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            fechaContratoCL.setCellValueFactory(new PropertyValueFactory<>("fechaContrato"));
            PrecioHoraCL.setCellValueFactory(new PropertyValueFactory<>("precioHora"));

            //Alinea la celda a la derecha el dato Precio/Hora
            PrecioHoraCL.setStyle("-fx-alignment: CENTER-RIGHT;");

            //Fortamato Fecha en datePickerFechaContrato dd/MM/yyyy
            formatoFecha();

            //Llenar la tabla de datos (Lista de todos los trabajadores)
            ObservableList<Trabajador> trabajadoresObservableList = FXCollections.observableArrayList(trabajadorGestion.buscarTodosLosTrabajadores());
            tablaTrabajadores.setItems(trabajadoresObservableList);

            //Añade acciones a los botones
            btnAnadir.setOnAction(this::handleBtnAnadir);
            btnModificar.setOnAction(this::handleBtnModificar);
            btnEliminar.setOnAction(this::handleBtnEliminar);
            btnLimpiar.setOnAction(this::handleBtnLimpiar);
            btnBuscar.setOnAction(this::handleBtnBuscar);
            btnInforme.setOnAction(this::handleImprimirInfotmeAction);
            //btnGestionarClientes.setOnAction(this::handleGestionClientesAction);

            //Añade acciones a los menuItems de la barra menu
            mnCerrarSecion.setOnAction(this::handleCerrarSesion);
            mnSalir.setOnAction(this::handleSalir);

            //Muestra la ventana
            stage.show();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Stage init error", e);
        }

    }

    /**
     * Llena al combo box de los nombre de los casos de usos
     */
    private void cargarFiltros() {
        ObservableList<String> cbxOptions = FXCollections.observableArrayList();
        cbxOptions.addAll("Todos", "Un trabajador", "Sin incidencias");
        cbxFiltro.setItems(cbxOptions);
        cbxFiltro.getSelectionModel().selectedItemProperty().addListener(this::cbListener);
    }

    /**
     * Se ejecuta cuando un campo de texto ha sido modificado. Identifica el
     * campo de texto modificado, comprueba que no se pase de los caracteres
     * máximos y llama a la funcion encargada de habilitar o deshabilitar los
     * botones.
     *
     * @param observable El valor que se observa.
     * @param oldValue El valor antiguo del observable.
     * @param newValue El valor nuevo del observable.
     */
    private void handleValidarTexto(ObservableValue observable, String oldValue, String newValue) {
        if (txtNombreCompleto.getText().length() > MAX_LENGHT) {
            String nombre = txtNombreCompleto.getText().substring(0, MAX_LENGHT);
            txtNombreCompleto.setText(nombre);

        }

        if (txtEmail.getText().length() > MAX_LENGHT) {
            String email = txtEmail.getText().substring(0, MAX_LENGHT);
            txtEmail.setText(email);
            lblErrorEmail.setText("Maximo de caracteres alcanzados 50");
            lblErrorEmail.setTextFill(Color.web("#FF0000"));

        }

        if (txtNombreUsuario.getText().length() > MAX_LENGHT_USER) {
            String usuario = txtNombreUsuario.getText().substring(0, MAX_LENGHT_USER);
            txtNombreUsuario.setText(usuario);

        }

        if (txtContrasenia.getText().length() > MAX_LENGHT_USER) {
            String contrasenia = txtContrasenia.getText().substring(0, MAX_LENGHT_USER);
            txtContrasenia.setText(contrasenia);

        }
        if (txtRepiteContrasenia.getText().length() > MAX_LENGHT_USER) {
            String repiteContrasenia = txtRepiteContrasenia.getText().substring(0, MAX_LENGHT_USER);
            txtRepiteContrasenia.setText(repiteContrasenia);

        }

        if (txtPrecioHora.getText().length() > MAX_LENGHT_CANTIDAD) {
            String precioHora = txtPrecioHora.getText().substring(0, new Integer((int) MAX_LENGHT_CANTIDAD));
            txtPrecioHora.setText(precioHora);
            lblErrorPrecioHora.setText("Maximo  de 2 cifras");
            lblErrorPrecioHora.setTextFill(Color.web("#FF0000"));
        }

        habilitarBotones();
    }

    /**
     * Metodo que habilita los botones
     */
    private void habilitarBotones() {
        btnBuscar.setDisable(txtBuscar.getText().isEmpty());

        if (!camposTextoVacios()) {
            btnAnadir.setDisable(false);
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);

        } else {
            btnAnadir.setDisable(true);
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
        }
        if (camposTextoConTexto()) {
            btnLimpiar.setDisable(false);
        } else {
            btnLimpiar.setDisable(true);
        }
    }

    /**
     * Metodo de seleccion de fecha que impide seleccionar una fecha anterior a
     * la actual
     *
     * @param datePikerFechaContrato
     */
    private void noSeleccionarFechaAnterior(DatePicker datePikerFechaContrato) {
        Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        LocalDate today = LocalDate.now();
                        setDisable(empty || item.compareTo(today) < 0);
                    }

                };
            }

        };
        datePikerFechaContrato.setDayCellFactory(callB);
    }

    /**
     * Metodo que carga al campo datePicker con el formato fecha dd/MM/yyyy
     */
    private void formatoFecha() {
        fechaContratoCL.setCellFactory(colum -> {
            TableCell<Trabajador, Date> cell = new TableCell<Trabajador, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(format.format(item));
                    }
                }
            };

            return cell;
        });
    }

    /**
     * Cuando se selecciona una fila de la tabla se rellenan los campos de texto
     * con la informacion del trabajador y se desactiva el boton "btnAñadir".
     *
     * @param observable El objeto siendo observado.
     * @param oldValue El valor viejo de la propiedad.
     * @param newValue El valor nuevo de la propiedad.
     */
    private void handleTablaSeleccionada(ObservableValue observable, Object oldValue, Object newValue) {
        lblErrorBuscar.setText("");
        if (newValue != null) {
            //  datePikerFechaContrato.setDisable(true);
            Trabajador trabajador = (Trabajador) newValue;
            txtNombreCompleto.setText(trabajador.getFullName());
            txtEmail.setText(trabajador.getEmail());
            txtPrecioHora.setText(Integer.toString(trabajador.getPrecioHora()));
            txtNombreUsuario.setText(trabajador.getLogin());
            txtContrasenia.setText(trabajador.getPassword());
            txtRepiteContrasenia.setText(trabajador.getPassword());
            datePikerFechaContrato.setValue(convertToLocalDateViaInstant(trabajador.getFechaContrato()));

            txtNombreCompleto.textProperty().addListener(this::handleValidarTextoModificado);
            txtEmail.textProperty().addListener(this::handleValidarTextoModificado);
            txtPrecioHora.textProperty().addListener(this::handleValidarTextoModificado);
            txtNombreUsuario.textProperty().addListener(this::handleValidarTextoModificado);
            txtContrasenia.textProperty().addListener(this::handleValidarTextoModificado);
            txtRepiteContrasenia.textProperty().addListener(this::handleValidarTextoModificado);

            btnAnadir.setDisable(true);
            btnModificar.setDisable(false);
            btnEliminar.setDisable(false);
        } else {
            txtNombreCompleto.setText("");
            txtEmail.setText("");
            txtPrecioHora.setText("");
            txtNombreUsuario.setText("");
            txtContrasenia.setText("");
            txtRepiteContrasenia.setText("");
            tablaTrabajadores.getSelectionModel().clearSelection();
            btnAnadir.setDisable(false);
            btnLimpiar.setDisable(false);
        }
        txtNombreCompleto.requestFocus();
    }

    /**
     * Método que añade un trabajador nuevo a la tabla.
     *
     * @param event
     */
    private void handleBtnAnadir(ActionEvent event) {
        boolean errorPatrones = patronesTextoBien();
        boolean errorDatePicker = datePickerVacio();
        boolean errorContrasenias = comprobarContrasenias();

        if (patronesTextoBien() && !errorDatePicker && !errorContrasenias) {
            try {

                //Comprueba si existe el login
                LOGGER.info("Usuario Controlador: Comprobando si existe el login");
                usuarioManager.buscarUsuarioPorLoginCrear(txtNombreUsuario.getText());
                //Comprueba si existe el email
                LOGGER.info("Usuario Controlador: Comprobando si existe el email");
                usuarioManager.buscarUsuarioPorEmailCrear(txtEmail.getText());

                Date date = new Date(System.currentTimeMillis());

                Trabajador nuvoTrabajador = new Trabajador();
                //Añade los datos escritos de los campos a los atribus correspondientes
                nuvoTrabajador.setFullName(txtNombreCompleto.getText());
                nuvoTrabajador.setEmail(txtEmail.getText());
                nuvoTrabajador.setPrecioHora(new Integer(txtPrecioHora.getText()));
                nuvoTrabajador.setLogin(txtNombreUsuario.getText());
                nuvoTrabajador.setPassword(txtContrasenia.getText());
                nuvoTrabajador.setStatus(ENABLED);
                nuvoTrabajador.setPrivilege(TRABAJADOR);
                nuvoTrabajador.setLastPasswordChange(date);
                nuvoTrabajador.setFechaContrato(convertToDateViaSqlDate(datePikerFechaContrato.getValue()));
                //Manda al trabajador para crearlos
                trabajadorGestion.createTrabajador(nuvoTrabajador);
                //Actualiza la tabla 
                ObservableList<Trabajador> trabajadoresObservableList = FXCollections.observableArrayList(trabajadorGestion.buscarTodosLosTrabajadores());
                tablaTrabajadores.setItems(trabajadoresObservableList);
                handleBtnLimpiar(event);
                lblErrorBuscar.setText("Se ha creado correctamente");
                lblErrorBuscar.setTextFill(Color.web("GREEN"));

            } catch (LoginExisteException le) {
                LOGGER.severe(le.getMessage());
                lblErrorNombreUsuario.setText("El login ya existe");
                lblErrorNombreUsuario.setTextFill(Color.web("#FF0000"));
            } catch (EmailExisteException ee) {
                LOGGER.severe(ee.getMessage());
                lblErrorEmail.setText("El email ya existe");
                lblErrorEmail.setTextFill(Color.web("#FF0000"));
            }
        } else {

        }
    }

    /**
     *
     * @param event
     */
    private void handleBtnModificar(ActionEvent event) {

        boolean errorDatePicker = datePickerVacio();
        boolean errorContrasenias = comprobarContrasenias();

        if (patronesTextoBien() && !errorDatePicker && !errorContrasenias) {
            Trabajador trabajadorSelecionado = tablaTrabajadores.getSelectionModel().getSelectedItem();
            trabajadorSelecionado.setFullName(txtNombreCompleto.getText());

            trabajadorSelecionado.setEmail(txtEmail.getText());
            trabajadorSelecionado.setPrecioHora(new Integer(txtPrecioHora.getText()));
            trabajadorSelecionado.setLogin(txtNombreUsuario.getText());
            trabajadorSelecionado.setFechaContrato(convertToDateViaSqlDate(datePikerFechaContrato.getValue()));
            trabajadorSelecionado.setPassword(txtContrasenia.getText());

            trabajadorGestion.editTrabajador(trabajadorSelecionado);
            tablaTrabajadores.getSelectionModel().clearSelection();
            tablaTrabajadores.refresh();
            lblErrorBuscar.setText("Se ha modificado correctamente ");
            lblErrorBuscar.setTextFill(Color.web("GREEN"));

        } else {
            lblErrorBuscar.setText("Error: No se puede modificar");
            lblErrorBuscar.setTextFill(Color.web("RED"));
        }

    }

    /**
     * Método que elimina un trabajador de la tabla.
     *
     * @param event
     */
    private void handleBtnEliminar(ActionEvent event) {
        boolean esta = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setContentText("¿Seguro que quieres eliminar el trabajador?");

        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);

        if (button == ButtonType.OK) {
            Trabajador seleccion = ((Trabajador) tablaTrabajadores.getSelectionModel().getSelectedItem());

            trabajadorGestion.removeTrabajador(seleccion);
            Collection<Trabajador> tbj = trabajadorGestion.buscarTodosLosTrabajadores();
            Collection<Trabajador> t = trabajadorGestion.buscarTrabajadorPorNombre(seleccion.getFullName());

            for (Trabajador trabajador : t) {
                for (Trabajador trabajador1 : tbj) {
                    if (trabajador.getFullName().equals(trabajador1.getFullName())) {
                        esta = true;
                        break;
                    } else {
                        esta = false;
                    }
                }
            }

            if (!esta) {
                LOGGER.info("Borrando");
                tablaTrabajadores.getItems().remove(seleccion);
                tablaTrabajadores.getSelectionModel().clearSelection();
                tablaTrabajadores.refresh();
                LOGGER.info("Borrado con exito");
            } else {
                LOGGER.info("ALERTA DE ERROR");
                errorBorrar();
                lblErrorBuscar.setText("ERROR: EL TRABAJADOR ESTA ACTIVO");
                lblErrorBuscar.setTextFill(Color.web("#FF0000"));
            }

            handleBtnLimpiar(event);
            lblErrorBuscar.setText("Trabajador eliminado correctamente");

        }

    }

    /**
     * Llama a un metodo para limpiar los campos de texto..
     *
     * @param event El evento de acción.
     */
    private void handleBtnLimpiar(ActionEvent event) {
        //limpiarCamposTexto();
        txtNombreCompleto.setText("");
        txtEmail.setText("");
        txtPrecioHora.setText("");
        datePikerFechaContrato.setValue(LocalDate.now());
        txtNombreUsuario.setText("");
        txtContrasenia.setText("");
        txtRepiteContrasenia.setText("");
        txtBuscar.setText("");

        lblErrorContrasenia.setText("");
        lblErrorBuscar.setText("");
        lblErrorEmail.setText("");
        lblErrorFechaContrato.setText("");
        lblErrorNombreCompleto.setText("");
        lblErrorNombreUsuario.setText("");
        lblErrorPrecioHora.setText("");
        lblErrorRepiteContrasenia.setText("");
        btnAnadir.setDisable(false);
        //btnLimpiar.setDisable(true);

        txtNombreCompleto.requestFocus();
        textoNuevo();
        tablaTrabajadores.getSelectionModel().clearSelection();
    }

    /**
     *
     * @param event
     */
    private void handleBtnBuscar(ActionEvent event) {
        //handleBtnLimpiar(event);
        boolean esta = false;
        if (cbxFiltro.getValue().equals("Todos")) {
            ObservableList<Trabajador> trabajadoresObservableList = FXCollections.observableArrayList(trabajadorGestion.buscarTodosLosTrabajadores());
            tablaTrabajadores.setItems(trabajadoresObservableList);
            tablaTrabajadores.refresh();

        }
        if (cbxFiltro.getValue().equals("Un trabajador")) {

            if (patronesBuscarBien()) {
                ObservableList<Trabajador> trabajadoresObservableList = FXCollections.observableArrayList(trabajadorGestion.buscarTrabajadorPorNombre(txtBuscar.getText()));
                tablaTrabajadores.setItems(trabajadoresObservableList);
                tablaTrabajadores.refresh();
            }

        }
        if (cbxFiltro.getValue().equals("Sin incidencias")) {
            ObservableList<Trabajador> trabajadoresObservableList = FXCollections.observableArrayList(trabajadorGestion.trabajadoresSinIncidencias());
            tablaTrabajadores.setItems(trabajadoresObservableList);
            tablaTrabajadores.refresh();

        }

    }

    /**
     *
     * @param event
     */
    private void handleImprimirInfotmeAction(ActionEvent event) {
        try {
            LOGGER.info("Comenzandoa imprimir los datos de la tabla Trabajador...");
            JasperReport report
                    = JasperCompileManager.compileReport(getClass()
                            .getResourceAsStream("/GESRE/archivos/informeReportGesre.jrxml"));
            //Data for the report: a collection of UserBean passed as a JRDataSource 
            //implementation 
            JRBeanCollectionDataSource dataItems
                    = new JRBeanCollectionDataSource((Collection<Trabajador>) this.tablaTrabajadores.getItems());
            //Map of parameter to be passed to the report
            Map<String, Object> parameters = new HashMap<>();
            //Fill report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            //Create and show the report window. The second parameter false value makes 
            //report window not to close app.
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
            // jasperViewer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        } catch (JRException ex) {
            //If there is an error show message and
            //log it.
            showErrorAlert("Error al imprimir:\n"
                    + ex.getMessage());
            LOGGER.log(Level.SEVERE,
                    "TrabajadorController: Error printing report: {0}",
                    ex.getMessage());
        }
    }

    /**
     *
     * @param event
     */
    private void handleGestionClientesAction(ActionEvent event) {

    }

    /**
     * Método que comprueba que si el datepicker está vacío.
     *
     * @return Variable que indica si el datepicker está vacío.
     */
    private boolean datePickerVacio() {
        boolean vacio = false;

        if (datePikerFechaContrato.getValue() == null) {
            lblErrorFechaContrato.setText("Introducir una fecha");
            lblErrorFechaContrato.setTextFill(Color.web("#FF0000"));

            vacio = true;
        } else {
            lblErrorFechaContrato.setText("");
        }

        return vacio;
    }

    private void cbListener(ObservableValue ov, String oldValue, String newValue) {
        btnBuscar.setDisable(false);

        if (cbxFiltro.getValue().equals("Todos")) {
            txtBuscar.setDisable(true);
            btnBuscar.setDisable(false);

        } else if (cbxFiltro.getValue().equals("Un trabajador")) {
            txtBuscar.setText("");
            txtBuscar.setDisable(false);
            btnBuscar.setDisable(false);
        } else if (cbxFiltro.getValue().equals("Sin incidencias")) {
            txtBuscar.setDisable(true);
            btnBuscar.setDisable(false);

        }
    }

    /**
     * Comprueba si algún campo de texto de Titulo, Autor, Editorial, Genero,
     * CantidadTotal o Isbn está vacio.
     *
     * @return Una variable indicando si hay algun campo de texto vacio o no.
     */
    private boolean camposTextoVacios() {
        if (txtNombreCompleto.getText().isEmpty() || txtEmail.getText().isEmpty()
                || txtPrecioHora.getText().isEmpty() || txtNombreUsuario.getText().isEmpty()
                || txtContrasenia.getText().isEmpty() || txtRepiteContrasenia.getText().isEmpty()) {
            btnAnadir.setDisable(false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Comprueba si algún campo de texto contiene texto o un checkbox está
     * seleccionado.
     *
     * @return Variable indicando si hay algun campo de texto con texto o no.
     */
    private boolean camposTextoConTexto() {
        if (!txtNombreCompleto.getText().isEmpty() || txtEmail.getText().isEmpty()
                || txtPrecioHora.getText().isEmpty() || txtNombreUsuario.getText().isEmpty()
                || txtContrasenia.getText().isEmpty() || txtRepiteContrasenia.getText().isEmpty()) {
            //btnAnadir.setDisable(false);
            //btnModificar.setDisable(false);
            //btnEliminar.setDisable(false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metodo que el facha Date a un DateSQL
     *
     * @param dateToConvert Date a cambio de DateSql
     * @return Devuelve el date en sql
     */
    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    /**
     * Metodo que convierte LocalDate date en formato Date
     *
     * @param dateToConvert para el cambio a date
     * @return un Date
     */
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Metodo que comprueba que si el nombre existe en la base de datos
     *
     * @param nombre El nombra del trabajador a buscar
     * @return Decuelve true o false si el nombre se ha encontrado
     */
    private boolean comprobrarNombre(String nombre) {
        Collection<Trabajador> trabajador = null;
        //   trabajador = trabajadorGestion.buscarTrabajadorPorNombre(nombre);

        if (trabajador.size() > 0) {
            for (Trabajador t : trabajador) {
                if (t.getFullName().equals(nombre)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Comprueba que los patrone de nombre completo, email, precio/hora, fecha,
     * nombre usuario contraseña y repetir contraseñas son correctos.
     *
     * @return Variable indicando si todos los patrones son correctos.
     */
    private boolean patronesTextoBien() {
        boolean patronesTextoBien = true;

        Matcher matcher = null;

        matcher = VALID_NOMBRE_COMPLETO.matcher(txtNombreCompleto.getText());
        if (!matcher.find()) {
            lblErrorNombreCompleto.setText("El Nombre completo sólo debe contener letras");
            lblErrorNombreCompleto.setTextFill(Color.web("#FF0000"));
            patronesTextoBien = false;
        } else {
            lblErrorNombreCompleto.setText("");
        }

        matcher = VALID_USUARIO.matcher(txtNombreUsuario.getText());
        if (!matcher.find()) {
            lblErrorNombreUsuario.setText("El usuario sólo debe contener letras y numeros");
            lblErrorNombreUsuario.setTextFill(Color.web("#FF0000"));
            patronesTextoBien = false;
        } else {
            lblErrorNombreUsuario.setText("");
        }
        matcher = VALID_EMAIL.matcher(txtEmail.getText());
        if (!matcher.find()) {
            lblErrorEmail.setText("Email es inválido ");
            lblErrorEmail.setTextFill(Color.web("#FF0000"));
            patronesTextoBien = false;
        } else {
            lblErrorEmail.setText("");
        }
        matcher = VALID_NUMERO.matcher(txtPrecioHora.getText());
        if (!matcher.find()) {
            lblErrorPrecioHora.setText("Deben ser numeros");
            lblErrorPrecioHora.setTextFill(Color.web("#FF0000"));
            patronesTextoBien = false;
        } else {
            lblErrorPrecioHora.setText("");
        }
        return patronesTextoBien;
    }

    /**
     * Cuadro de diálogo que se abre al pulsar la menuItem Salir de la pantalla
     * para confirmar si se quiere cerrar la aplicación.
     *
     * @param event El evento de acción.
     */
    private void handleSalir(ActionEvent event) {
        try {
            LOGGER.info("Trabajador Controlador: Salir programa");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Salir");
            alert.setHeaderText(null);
            alert.setContentText("¿Seguro que quieres cerrar la ventana?");

            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get().equals(ButtonType.OK)) {
                LOGGER.info("Trabajador Controlador: Cerrando aplicacion");
                stage.close();
                Platform.exit();
            } else {
                event.consume();
                alert.close();
            }
        } catch (Exception ex) {

            LOGGER.log(Level.SEVERE,
                    "UI GestionUsuariosController: Error printing report: {0}",
                    ex.getMessage());
        }

    }

    /**
     * Al pulsar el menuItem Cerrar Secion cierra la venta de gestion trabajador
     * y abre SignIn
     *
     * @param event El evento de acción.
     */
    private void handleCerrarSesion(ActionEvent event) {

        LOGGER.info("Trabajador Controlador: Salir programa para SignIn");

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Salir");
            alert.setHeaderText(null);
            alert.setContentText("¿Seguro que quieres cerrar la ventana?");

            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get().equals(ButtonType.OK)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GESRE/vistas/SignIn.fxml"));
                Parent root = (Parent) loader.load();
                SignInController controller = ((SignInController) loader.getController());
                controller.setStage(stage);
                controller.initStage(root);
            } else {
                event.consume();
                alert.close();
            }
        } catch (Exception ex) {

            LOGGER.log(Level.SEVERE,
                    "UI GestionUsuariosController: Error printing report: {0}",
                    ex.getMessage());
        }

    }

    /**
     * Cuadro de diálogo que se abre al pulsar la "X" de la pantalla para
     * confirmar si se quiere cerrar la aplicación.
     *
     * @param event El evento de acción.
     *
     *
     */
    private void cerrarVentana(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Salir");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que quieres cerrar la ventana?");

        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().equals(ButtonType.OK)) {
            LOGGER.info(
                    "TrabajadorControlador:  Cerrando aplicacion");
            stage.close();
            Platform.exit();
        } else {
            event.consume();
            alert.close();
        }
    }

    /**
     * Alerta de error
     *
     * @param errorMsg mensaje
     */
    protected void showErrorAlert(String errorMsg) {
        //Shows error dialog.
        Alert alert = new Alert(Alert.AlertType.ERROR,
                errorMsg,
                ButtonType.OK);
    }

    /**
     * Metodo que comprueba que los campos contraseña y repetir contraseña estan
     * escritas iguales
     *
     * @return Si estan correctamente escrito
     */
    private boolean comprobarContrasenias() {
        boolean error = false;

        if (!txtContrasenia.getText().equals(txtRepiteContrasenia.getText())) {
            lblErrorRepiteContrasenia.setText("Las contraseñas no coinciden");
            lblErrorRepiteContrasenia.setTextFill(Color.web("#FF0000"));

            error = true;
        } else {
            lblErrorRepiteContrasenia.setText("");
        }

        return error;
    }

    /**
     * Metodo que alerta cuando para el error de borrado de un trabajadors
     */
    private void errorBorrar() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText("EL TRABAJADOR NO SE PUEDE BORRAR\n"
                + "Tiene incidencias aun sin terminar o tiene piezas creadas");
        alert.showAndWait();
    }

    /**
     * Se ejecuta cuando el campo de texto de busqueda llega maxido de
     * caracteres.
     *
     * @param observable El valor que se observa.
     * @param oldValue El valor antiguo del observable.
     * @param newValue El valor nuevo del observable.
     */
    private void validarCampoTextoBuscar(ObservableValue observable, String oldValue, String newValue) {
        if (txtBuscar.getText().length() > MAX_LENGHT) {
            String nombre = txtBuscar.getText().substring(0, MAX_LENGHT);
            txtBuscar.setText(nombre);
            lblErrorBuscar.setText("Maximo de caracteres 25 alcanzados");
            lblErrorBuscar.setTextFill(Color.web("#FF0000"));
        } else {
            lblErrorBuscar.setText("");
        }

    }

    /**
     * Comprueba que el patron del campo de texto de busqueda es correcto.
     *
     * @return Variable indicando el patron es correcto.
     */
    private boolean patronesBuscarBien() {
        boolean patronesBuscarBien = true;

        Matcher matcher = null;
        matcher = VALID_NOMBRE_COMPLETO.matcher(txtBuscar.getText());
        if (!matcher.find()) {
            lblErrorBuscar.setText("El nombre sólo debe contener letras");
            lblErrorBuscar.setTextFill(Color.web("#FF0000"));
            patronesBuscarBien = false;
        } 
        
        if (txtBuscar.getText().isEmpty()) {
             lblErrorBuscar.setText("Error: Escribe un nombre a buscar");
            lblErrorBuscar.setTextFill(Color.web("#FF0000"));
            patronesBuscarBien = false;
        }
        return patronesBuscarBien;
    }

    /**
     * Se ejecuta cuando un campo de texto ha sido modificado. Identifica el
     * campo de texto modificado, comprueba que no se pase de los caracteres
     * máximos y llama a la funcion encargada de habilitar o deshabilitar los
     * botones.
     *
     * @param observable El valor que se observa.
     * @param oldValue El valor antiguo del observable.
     * @param newValue El valor nuevo del observable.
     */
    private void handleValidarTextoModificado(ObservableValue observable, String oldValue, String newValue) {
        if (txtNombreCompleto.getText().length() > MAX_LENGHT) {
            String nombre = txtNombreCompleto.getText().substring(0, MAX_LENGHT);
            txtNombreCompleto.setText(nombre);

        }

        if (txtEmail.getText().length() > MAX_LENGHT) {
            String email = txtEmail.getText().substring(0, MAX_LENGHT);
            txtEmail.setText(email);
            lblErrorEmail.setText("Maximo de caracteres alcanzados 50");
            lblErrorEmail.setTextFill(Color.web("#FF0000"));

        }

        if (txtNombreUsuario.getText().length() > MAX_LENGHT_USER) {
            String usuario = txtNombreUsuario.getText().substring(0, MAX_LENGHT_USER);
            txtNombreUsuario.setText(usuario);

        }

        if (txtContrasenia.getText().length() > MAX_LENGHT_USER) {
            String contrasenia = txtContrasenia.getText().substring(0, MAX_LENGHT_USER);
            txtContrasenia.setText(contrasenia);

        }
        if (txtRepiteContrasenia.getText().length() > MAX_LENGHT_USER) {
            String repiteContrasenia = txtRepiteContrasenia.getText().substring(0, MAX_LENGHT_USER);
            txtRepiteContrasenia.setText(repiteContrasenia);

        }

        if (txtPrecioHora.getText().length() > MAX_LENGHT_CANTIDAD) {
            String precioHora = txtPrecioHora.getText().substring(0, new Integer((int) MAX_LENGHT_CANTIDAD));
            txtPrecioHora.setText(precioHora);

        }

        habilitarBotonesM();
    }

    /**
     * Metodo que habilita y desabilita los botones cuando se seleccciona un
     * trabajador
     */
    private void habilitarBotonesM() {
        btnBuscar.setDisable(txtBuscar.getText().isEmpty());

        if (!camposTextoVacios()) {
            btnAnadir.setDisable(true);
            btnModificar.setDisable(false);
            btnEliminar.setDisable(false);

        } else {
            btnAnadir.setDisable(true);
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
        }
        if (camposTextoConTexto()) {
            btnLimpiar.setDisable(false);
        } else {
            btnLimpiar.setDisable(true);
        }
    }

    /**
     * Metodoque comprueba las validacion de los campos de texto
     *
     */
    private void textoNuevo() {
        txtNombreCompleto.textProperty().addListener(this::handleValidarTexto);
        txtEmail.textProperty().addListener(this::handleValidarTexto);
        txtPrecioHora.textProperty().addListener(this::handleValidarTexto);
        txtNombreUsuario.textProperty().addListener(this::handleValidarTexto);
        txtContrasenia.textProperty().addListener(this::handleValidarTexto);
        txtRepiteContrasenia.textProperty().addListener(this::handleValidarTexto);

    }

}
