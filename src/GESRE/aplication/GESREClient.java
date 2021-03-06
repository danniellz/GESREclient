package GESRE.aplication;

import GESRE.controller.GestionClientesController;
import GESRE.controller.GestionTrabajadorViewController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import GESRE.controller.SignInController;
import GESRE.entidades.SignIn;

/**
 * class responsible for starting the application
 *
 * @author Daniel Brizuela
 * @version 1.0
 */
public class GESREClient extends Application {

    //LOGGER
    private static final Logger LOG = Logger.getLogger(SignInController.class.getName());

    /**
     * This is the first window (SignIn)
     *
     * @param primaryStage stage object (window)
     * @throws Exception throws an error if the start method fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            LOG.info("Iniciando Ventana...");
            //Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GESRE/vistas/SignIn.fxml"));
            Parent root = (Parent) loader.load();
            //Get controller
            SignInController controlador =((SignInController) loader.getController());
            //Set the stage
            controlador.setStage(primaryStage);
            //initialize the window
            controlador.initStage(root);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error Starting SignIn window", ex);
        }
    }

    /**
     * Main class, start the application
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
