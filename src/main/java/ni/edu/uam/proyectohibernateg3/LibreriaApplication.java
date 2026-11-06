package ni.edu.uam.proyectohibernateg3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import ni.edu.uam.proyectohibernateg3.database.JPAUtil;

import java.io.IOException;
import java.util.Optional;

public class LibreriaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        if (System.getenv("DB_PASSWORD") == null || System.getenv("DB_PASSWORD").isBlank()) {
            Dialog<String> dialogo = new Dialog<>();
            dialogo.setTitle("Conectar a PostgreSQL");
            dialogo.setHeaderText("Contraseña del usuario postgres para bdbiblioteca");
            PasswordField campoPassword = new PasswordField();
            campoPassword.setPromptText("Contraseña");
            dialogo.getDialogPane().setContent(campoPassword);
            dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialogo.setResultConverter(boton -> boton == ButtonType.OK ? campoPassword.getText() : null);
            Optional<String> resultado = dialogo.showAndWait();
            if (resultado.isEmpty() || resultado.get().isBlank()) {
                return;
            }
            JPAUtil.configurarPassword(resultado.get());
        }

        try {
            JPAUtil.iniciar();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            Throwable causa = ex;
            while (causa.getCause() != null) {
                causa = causa.getCause();
            }
            Alert alerta = new Alert(Alert.AlertType.ERROR,
                    "No se pudo abrir bdbiblioteca: " + causa.getMessage());
            alerta.setHeaderText("Error de conexión");
            alerta.showAndWait();
            return;
        }

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

    @Override
    public void stop() {
        JPAUtil.cerrar();
    }
}
