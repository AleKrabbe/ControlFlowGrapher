package br.com.cfg.controlflowgrapher;

import br.com.cfg.controlflowgrapher.controller.FXMLController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/fxml/Scene.fxml").openStream());
        FXMLController controller = (FXMLController) fxmlLoader.getController();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll("/styles/Styles.css", "/styles/java-keywords.css");
        
        stage.setTitle("Control Flow Grapher - CFG (UFMS)");
        stage.setScene(scene);
        stage.show();

    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
