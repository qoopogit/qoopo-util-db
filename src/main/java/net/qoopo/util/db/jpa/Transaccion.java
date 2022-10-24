package net.qoopo.util.db.jpa;

import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Maneja las transacciones
 *
 * @author Alberto
 */
public class Transaccion {

    private String dataSourceName = "cntPU";
    public static final Logger log = Logger.getLogger("cnt-util-db");
    //
    //http://docs.jboss.org/hibernate/stable/entitymanager/reference/en/html/transactions.html
    //según la pagina superior, indica que nunca hay qu usar un entitymanager por aplicación ni uno entitymanager por sesion de usuario
    private EntityManagerFactory emf;

    public static Transaccion get(String dataSourceName) {
        return new Transaccion(dataSourceName);
    }

    public EntityManager getEntityManager(String dataSourceName) {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(dataSourceName);
        }

        if (emf != null) {
            return emf.createEntityManager();
        } else {
            log.severe("NO SE HA CREADO EN ENTITY MANAGER FACTORY, DEVUELVO NULO AL ENTITY MANAGER");
            return null;
        }
    }
    private EntityManager em;

//    private long tInicio;//tiempo inicio de la operacion, cuando abre la conexion
    private Transaccion(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * Abre una sesion para la conexion
     */
    public void abrir() {
        em = getEntityManager(dataSourceName);
    }

    /**
     * Abre una sesion y crea una transaccion
     *
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     */
    public void begin() throws Exception {
        abrir();
        em.getTransaction().begin();
    }

    /**
     * Termina una sesion y una transaccion
     *
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     */
    public void commit() throws Exception {
        if (em != null) {
            try {
                em.getTransaction().commit();
            } finally {
                cerrar();
            }
        }
    }

    /**
     * Cancela la transaccion y cierra la sesion
     *
     */
    public void rollback() {
        if (em != null) {
            try {
                em.getTransaction().rollback();
            } finally {
                cerrar();
            }
        }
    }

    /**
     * Cierra la sesion
     */
    public void cerrar() {
        if (em != null) {
            if (em.isOpen()) {
                em.close();
            } else {
                log.severe("Se intentó cerrar un entitymanager ya cerrado");
            }
        }
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
