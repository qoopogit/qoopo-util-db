package ec.gob.cnt.util.db.daos;

import ec.gob.cnt.util.db.jpa.JPA;
import ec.gob.cnt.util.db.jpa.Transaccion;
import ec.gob.cnt.util.db.jpa.exceptions.IllegalOrphanException;
import ec.gob.cnt.util.db.jpa.exceptions.NonexistentEntityException;
import ec.gob.cnt.util.db.jpa.exceptions.RollbackFailureException;

/**
 * Clase que permite realiza las operaciones basicas CRUD sobre cualquier
 * entidad de la base de datos
 *
 * @author alberto
 */
public interface GenericDAO {

    public static void create(Transaccion transaccion, Object item) throws Exception, RollbackFailureException {
        JPA.get().setEm(transaccion.getEm()).setClase(item.getClass()).crear(item);
    }

    public static Object edit(Transaccion transaccion, Object item) throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        return JPA.get().setEm(transaccion.getEm()).setClase(item.getClass()).editar(item);
    }

    public static void delete(Transaccion transaccion, Object item) throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        JPA.get().setEm(transaccion.getEm()).setClase(item.getClass()).eliminar(item);
    }

    public static Object find(Transaccion transaccion, Class entityClass, Long id) {
        return JPA.get().setEm(transaccion.getEm()).setClase(entityClass).buscar(id);
    }

}
