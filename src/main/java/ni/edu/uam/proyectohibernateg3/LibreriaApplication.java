package ni.edu.uam.proyectohibernateg3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LibreriaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibreriaApplication.class.getResource("libros-view.fxml"));
        // Sin ancho/alto fijo: la escena toma el tamaño definido en el FXML
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Registro de Libros");
        stage.setScene(scene);
        stage.show();
        // Evita que la ventana se achique más de su tamaño inicial
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }
}
