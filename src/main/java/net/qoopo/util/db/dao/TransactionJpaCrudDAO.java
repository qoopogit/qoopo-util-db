package net.qoopo.util.db.dao;

import java.util.List;
import java.util.Optional;

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
public class TransactionJpaCrudDAO<T, ID> implements ITransactionCrudDAO<T, ID> {

    private JPA<T, ID> jpa;

    private Class<T> entityClass = null;

    public TransactionJpaCrudDAO() {
        jpa = new JPA<>();
    }

    public TransactionJpaCrudDAO(Class<T> entityClass) {
        jpa = new JPA<>();
        this.entityClass = entityClass;
    }

    @Override
    public T create(JpaTransaction transaccion, T item) throws Exception, RollbackFailureException {
        if (entityClass == null)
            entityClass = (Class<T>) item.getClass();
        return jpa.setEm(transaccion.getEm()).setEntityClass(entityClass).create(item);
    }

    @Override
    public T edit(JpaTransaction transaccion, T item)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        if (entityClass == null)
            entityClass = (Class<T>) item.getClass();
        return jpa.setEm(transaccion.getEm()).setEntityClass(entityClass).edit(item);
    }

    @Override
    public void delete(JpaTransaction transaccion, T item)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        if (entityClass == null)
            entityClass = (Class<T>) item.getClass();
        jpa.setEm(transaccion.getEm()).setEntityClass(entityClass).delete(item);
    }

    @Override
    public void deletebyId(JpaTransaction transaccion, ID id)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        jpa.setEm(transaccion.getEm()).setEntityClass(entityClass).deletebyId(id);
    }

    @Override
    public Optional<T> find(JpaTransaction transaccion, ID id) {
        return jpa.setEm(transaccion.getEm()).setEntityClass(entityClass).find(id);
    }

    @Override
    public List<T> findAll(JpaTransaction transaccion) {
        return jpa.setEm(transaccion.getEm()).setEntityClass(entityClass).findEntities();
    }

    @Override
    public List<T> findAll(JpaTransaction transaccion, int maxResults, int firstResult) {
        return jpa.setEm(transaccion.getEm()).setEntityClass(entityClass).findEntities(maxResults,
                firstResult);
    }

    // int maxResults, int firstResult
}
