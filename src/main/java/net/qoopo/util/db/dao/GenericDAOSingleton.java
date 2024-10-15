package net.qoopo.util.db.dao;

import net.qoopo.util.db.jpa.JPA;
import net.qoopo.util.db.jpa.Transaccion;
import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;

/**
 * Clase que permite realiza las operaciones basicas CRUD sobre cualquier
 * entidad de la base de datos
 *
 * @author alberto
 */
public class GenericDAOSingleton {

    public static Object create(Transaccion transaccion, Object item) throws Exception, RollbackFailureException {
        return JPA.get().setEm(transaccion.getEm()).setClase(item.getClass()).create(item);
    }

    public static Object edit(Transaccion transaccion, Object item) throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        return JPA.get().setEm(transaccion.getEm()).setClase(item.getClass()).edit(item);
    }

    public static void delete(Transaccion transaccion, Object item) throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        JPA.get().setEm(transaccion.getEm()).setClase(item.getClass()).delete(item);
    }

    public static Object find(Transaccion transaccion, Class entityClass, Long id) {
        return JPA.get().setEm(transaccion.getEm()).setClase(entityClass).find(id).get();
    }

}
