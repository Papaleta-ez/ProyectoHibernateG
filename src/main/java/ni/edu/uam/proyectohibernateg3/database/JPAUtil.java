package ni.edu.uam.proyectohibernateg3.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPAUtil {
    private static EntityManagerFactory entityManagerFactory;
    private static String passwordConfigurada;

    public static void configurarPassword(String password) {
        passwordConfigurada = password;
    }

    public static synchronized void iniciar() {
        if (entityManagerFactory == null) {
            String password = passwordConfigurada != null ? passwordConfigurada : System.getenv("DB_PASSWORD");
            if (password == null || password.isBlank()) {
                throw new RuntimeException("DB_PASSWORD environment variable is empty");
            }
            Map<String, Object> propiedades = new HashMap();
            propiedades.put("jakarta.persistence.jdbc.password", password);
            entityManagerFactory = Persistence.createEntityManagerFactory("bibliotecaPU", propiedades);
        }
    }

    public static EntityManager getEntityManager() {
        iniciar();
        return entityManagerFactory.createEntityManager();
    }

    public static synchronized void cerrar(){
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }
}
