package ni.edu.uam.proyectohibernateg3.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.uam.proyectohibernateg3.dao.LibroDAO;
import ni.edu.uam.proyectohibernateg3.model.Libro;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class LibreriaController {

    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAutor;
    @FXML
    private TextField txtAnio;
    @FXML
    private TextField txtPrecio;

    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnCargar;
    @FXML
    private Button btnGuardar;

    @FXML
    private TableView<Libro> tblLibros;

    @FXML
    private TableColumn<Libro, Long> colId;
    @FXML
    private TableColumn<Libro, String> colTitulo;
    @FXML
    private TableColumn<Libro, String> colAutor;
    @FXML
    private TableColumn<Libro, Integer> colAnio;
    @FXML
    private TableColumn<Libro, BigDecimal> colPrecio;

    private final LibroDAO libroDAO = new LibroDAO();

    private Long idSeleccionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        tblLibros.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                seleccionarLibro(newValue);
            }
        });

        actualizarBotones();
    }

    @FXML
    private void guardarLibro() {
        try
            {
            Libro libro = obtenerFormulario();
            if (libro == null) {
                return;
            }
            libroDAO.guardar(libro);
            limpiarCampos();
            cargarLibros();
            }catch(Exception ex){
            ex.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "No se pudo guardar el libro: " + ex.getMessage());
        }
    }

    @FXML
    private void cargarLibros() {
        try {
            List<Libro> Libros = libroDAO.listar();
            limpiarCampos();
            tblLibros.setItems(FXCollections.observableList(Libros));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void modificarLibro() {
        Libro libro = obtenerFormulario();
        if (libro == null) {
            return;
        }
        libro.setId(idSeleccionado);
        try {
            libroDAO.actualizar(libro);
            limpiarCampos();
            cargarLibros();
        } catch (RuntimeException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "No se pudo modificar el libro: " + ex.getMessage());
        }
    }

    @FXML
    private void eliminarLibro() {
        try{
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmacion de eliminacion de datos");
            confirmacion.setHeaderText("Eliminar libro");
            confirmacion.setContentText("¿Estas seguro que desea eliminar el registro seleccionado ?");

            Optional<ButtonType> resultado =confirmacion.showAndWait();
            if(resultado.isEmpty() || resultado.get() != ButtonType.OK) {
                return;
            }
            boolean eliminado = libroDAO.eliminar(idSeleccionado);
            if (!eliminado){
                mostrarAlerta(Alert.AlertType.WARNING, "El libro ya no existe en la base de datos.");
                return;
            }
            limpiarCampos();
            cargarLibros();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void limpiarCampos() {
        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtPrecio.clear();
        idSeleccionado = null;
        tblLibros.getSelectionModel().clearSelection();
        actualizarBotones();
    }

    private void seleccionarLibro(Libro libro) {
        idSeleccionado = libro.getId();
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtPrecio.setText(String.valueOf(libro.getPrecio()));
        txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
        actualizarBotones();
    }

    private void actualizarBotones() {
        boolean sinSeleccion = idSeleccionado == null;
        btnModificar.setDisable(sinSeleccion);
        btnEliminar.setDisable(sinSeleccion);
    }

    // Lee y valida los campos; devuelve null (y muestra un aviso) si algún dato es inválido
    private Libro obtenerFormulario() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        if (titulo.isEmpty() || autor.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "El título y el autor son obligatorios.");
            return null;
        }

        int anio;
        try {
            anio = Integer.parseInt(txtAnio.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "El año debe ser un número entero.");
            return null;
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(txtPrecio.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "El precio debe ser un número válido (ej. 250.50).");
            return null;
        }

        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnioPublicacion(anio);
        libro.setPrecio(precio);
        return libro;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alerta = new Alert(tipo, mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}
