package net.qoopo.util.db.dao;

import java.util.List;
import java.util.Optional;

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
public class CrudDAO<T, S> implements ICrudDAO<T, S> {

    private JPA<T> jpa;

    private Class<T> entityClass = null;

    public CrudDAO() {
        jpa = new JPA<>();
    }

    public CrudDAO(Class<T> entityClass) {
        jpa = new JPA<>();
        this.entityClass = entityClass;
    }

    public T create(Transaccion transaccion, T item) throws Exception, RollbackFailureException {
        if (entityClass == null)
            entityClass = (Class<T>) item.getClass();
        return jpa.setEm(transaccion.getEm()).setClase(entityClass).create(item);
    }

    public T edit(Transaccion transaccion, T item)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        if (entityClass == null)
            entityClass = (Class<T>) item.getClass();
        return jpa.setEm(transaccion.getEm()).setClase(entityClass).edit(item);
    }

    public void delete(Transaccion transaccion, T item)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        if (entityClass == null)
            entityClass = (Class<T>) item.getClass();
        jpa.setEm(transaccion.getEm()).setClase(entityClass).delete(item);
    }

    public void deletebyId(Transaccion transaccion, S id)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        jpa.setEm(transaccion.getEm()).setClase(entityClass).deletebyId(id);
    }

    public Optional<T> find(Transaccion transaccion, S id) {
        return jpa.setEm(transaccion.getEm()).setClase(entityClass).setClase(entityClass).find(id);
    }

    @Override
    public List<T> findAll(Transaccion transaccion) {
        return jpa.setEm(transaccion.getEm()).setClase(entityClass).setClase(entityClass).findEntities();
    }

    @Override
    public List<T> findAll(Transaccion transaccion, int maxResults, int firstResult) {
        return jpa.setEm(transaccion.getEm()).setClase(entityClass).setClase(entityClass).findEntities(maxResults,
                firstResult);
    }

    // int maxResults, int firstResult
}
