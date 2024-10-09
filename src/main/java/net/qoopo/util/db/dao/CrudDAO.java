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
public class CrudDAO<T> implements ICrudDAO<T> {

    private JPA<T> jpa;

    private Class<T> entityClass;

    public CrudDAO(Class<T> entityClass) {
        jpa = new JPA<>();
        this.entityClass = entityClass;
    }

    public T create(Transaccion transaccion, T item) throws Exception, RollbackFailureException {
        return jpa.setEm(transaccion.getEm()).setClase(entityClass).crear(item);
    }

    public T edit(Transaccion transaccion, T item)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        return jpa.setEm(transaccion.getEm()).setClase(entityClass).editar(item);
    }

    public void delete(Transaccion transaccion, T item)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        jpa.setEm(transaccion.getEm()).setClase(entityClass).eliminar(item);
    }

    public T find(Transaccion transaccion, Long id) {
        return jpa.setEm(transaccion.getEm()).setClase(entityClass).setClase(entityClass).buscar(id);
    }

}
