module ni.edu.uam.proyectohibernateg {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    opens ni.edu.uam.proyectohibernateg3 to javafx.fxml;
    exports ni.edu.uam.proyectohibernateg3;
    exports ni.edu.uam.proyectohibernateg3.controller;
    opens ni.edu.uam.proyectohibernateg3.controller to javafx.fxml;
    // PropertyValueFactory lee los getters de Libro por reflexión
    opens ni.edu.uam.proyectohibernateg3.model;
}
