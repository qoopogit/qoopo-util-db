package net.qoopo.util.db.dao;

import net.qoopo.util.db.jpa.JPA;
import net.qoopo.util.db.jpa.JpaTransaction;
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

    public static Object create(JpaTransaction transaccion, Object item) throws Exception, RollbackFailureException {
        return JPA.get().setEm(transaccion.getEm()).setEntityClass(item.getClass()).create(item);
    }

    public static Object edit(JpaTransaction transaccion, Object item) throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        return JPA.get().setEm(transaccion.getEm()).setEntityClass(item.getClass()).edit(item);
    }

    public static void delete(JpaTransaction transaccion, Object item) throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        JPA.get().setEm(transaccion.getEm()).setEntityClass(item.getClass()).delete(item);
    }

    public static Object find(JpaTransaction transaccion, Class entityClass, Long id) {
        return JPA.get().setEm(transaccion.getEm()).setEntityClass(entityClass).find(id).get();
    }

}
