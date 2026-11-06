package ni.edu.uam.proyectohibernateg3.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ni.edu.uam.proyectohibernateg3.database.JPAUtil;
import ni.edu.uam.proyectohibernateg3.model.Libro;

import java.util.List;

public class LibroDAO {
    public void guardar(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            // Iniciar la transacción en PostgreSQL
            tx.begin();
            // Decirle a Hibernate que prepare la persistencia del objeto.
            em.persist(libro);
            // Confirmar los cambios en la base de datos
            tx.commit();
        }catch(RuntimeException ex){
            if(tx.isActive()){
                tx.rollback();
            }
            throw ex;
        }finally {
            em.close();
        }
    }

    public void actualizar(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            // merge copia los datos del objeto sobre el registro existente con el mismo id
            em.merge(libro);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // Devuelve true si el libro existía y se eliminó, false si no se encontró
    public boolean eliminar(long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Libro libro = em.find(Libro.class, id);
            if (libro == null) {
                tx.rollback();
                return false;
            }
            em.remove(libro);
            tx.commit();
            return true;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Libro> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT l FROM Libro l ORDER BY l.id", Libro.class).getResultList();
        }
        finally {
            em.close();
        }
    }
}