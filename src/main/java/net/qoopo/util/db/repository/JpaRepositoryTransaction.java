package net.qoopo.util.db.repository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import net.qoopo.util.db.dao.TransactionJpaCrudDAO;
import net.qoopo.util.db.jpa.JpaTransaction;
import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;

/**
 * Repositorio JPA para las operaciones CRUD
 */
public class JpaRepositoryTransaction<T, ID> implements CrudRepository<T, ID> {
    public static final Logger log = Logger.getLogger("jparepository");

    protected TransactionJpaCrudDAO<T, ID> crudDao;
    protected String datasourceName;

    public JpaRepositoryTransaction(String datasourceName) {
        crudDao = new TransactionJpaCrudDAO<>();
        this.datasourceName = datasourceName;
    }

    public JpaRepositoryTransaction(Class<T> entityClass, String datasourceName) {
        crudDao = new TransactionJpaCrudDAO<>(entityClass);
        this.datasourceName = datasourceName;
    }

    /**
     * Crea una lista de entidades en una sola transaccion
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     */
    public void saveAll(Iterable<T> item)
            throws NonexistentEntityException, RollbackFailureException, Exception {
        if (item == null) {
            return;
        }
        JpaTransaction trx = JpaTransaction.get(datasourceName);
        trx.begin();
        try {
            for (T t : item) {
                crudDao.create(trx, t);
            }
            trx.commit();
        } catch (RollbackFailureException | NonexistentEntityException e) {
            trx.rollback();
            throw e;
        }
    }

    /**
     * Crea una entidad
     *
     * @param item
     * @return
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
     */
    @Override
    public T save(T item)
            throws NonexistentEntityException, RollbackFailureException, Exception {
        if (item == null) {
            return null;
        }
        JpaTransaction trx = JpaTransaction.get(datasourceName);
        trx.begin();
        try {
            // item =dao.create(trx, item);
            item = crudDao.edit(trx, item);
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
        return item;
    }

    /**
     * Elimina una lista de entidades en una transaccion
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.IllegalOrphanException
     */
    @Override
    public void deleteAll(Iterable<T> item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return;
        }
        JpaTransaction trx = JpaTransaction.get(datasourceName);
        trx.begin();
        try {
            for (T t : item) {
                crudDao.delete(trx, t);
            }
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
    }

    /**
     * Elimina una entidad
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.IllegalOrphanException
     */
    @Override
    public void delete(T item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return;
        }
        JpaTransaction trx = JpaTransaction.get(datasourceName);
        trx.begin();
        try {
            crudDao.delete(trx, item);
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
    }

    @Override
    public void deleteById(ID id)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (id == null) {
            return;
        }
        JpaTransaction trx = JpaTransaction.get(datasourceName);
        trx.begin();
        try {
            crudDao.deletebyId(trx, id);
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
    }

    /**
     *
     * @param entityClass
     * @param id
     * @return
     */
    @Override
    public Optional<T> find(ID id) {
        JpaTransaction trx = JpaTransaction.get(datasourceName);
        trx.open();
        Optional<T> item = null;
        try {
            item = crudDao.find(trx, id);
            trx.close();
        } catch (Exception e) {
            trx.close();
        }
        return item;
    }

    @Override
    public List<T> findAll() {
        List<T> returnValue = null;
        try {
            JpaTransaction trx = JpaTransaction.get(datasourceName);
            trx.begin();
            try {
                returnValue = crudDao.findAll(trx);
                trx.commit();
            } catch (IllegalOrphanException | NonexistentEntityException | RollbackFailureException e) {
                trx.rollback();
            }
        } catch (Exception ex) {
            //
        }
        return returnValue;
    }

    @Override
    public List<T> findAll(int maxResults, int firstResult) {
        List<T> returnValue = null;
        try {
            JpaTransaction trx = JpaTransaction.get(datasourceName);
            trx.begin();
            try {
                returnValue = crudDao.findAll(trx, maxResults, firstResult);
                trx.commit();
            } catch (IllegalOrphanException | NonexistentEntityException | RollbackFailureException e) {
                trx.rollback();
            }
        } catch (Exception ex) {
            //
        }
        return returnValue;
    }
}
