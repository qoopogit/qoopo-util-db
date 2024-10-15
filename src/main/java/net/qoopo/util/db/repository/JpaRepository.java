package net.qoopo.util.db.repository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import net.qoopo.util.db.dao.CrudDAO;
import net.qoopo.util.db.jpa.Transaccion;
import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;

public class JpaRepository<T, S> implements IJpaRepository<T, S> {
    public static final Logger log = Logger.getLogger("jparepository");

    private CrudDAO<T, S> dao;
    private String datasourceName;

    public JpaRepository(String datasourceName) {
        dao = new CrudDAO<>();
        this.datasourceName = datasourceName;
    }

    public JpaRepository(Class<T> entityClass, String datasourceName) {
        dao = new CrudDAO<>(entityClass);
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
        Transaccion trx = Transaccion.get(datasourceName);
        trx.begin();
        try {
            for (T t : item) {
                dao.create(trx, t);
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
    public T save(T item)
            throws NonexistentEntityException, RollbackFailureException, Exception {
        if (item == null) {
            return null;
        }
        Transaccion trx = Transaccion.get(datasourceName);
        trx.begin();
        try {
            // item =dao.create(trx, item);
            item = dao.edit(trx, item);
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
        return item;
    }

    /**
     * Crea una entidad sin lanzar excepcion
     *
     * @param item
     * @return
     */
    public T saveWithoutError(T item) {
        try {
            if (item == null) {
                return null;
            }
            Transaccion trx = Transaccion.get(datasourceName);
            trx.begin();
            try {
                // dao.create(trx, item);
                item = (T) dao.edit(trx, item);
                trx.commit();
            } catch (Exception e) {
                trx.rollback();
            }
            return item;
        } catch (Exception ex) {
            //
        }
        return null;
    }

    /**
     *
     * @param entityClass
     * @param id
     * @return
     */
    public Optional<T> find(S id) {
        Transaccion trx = Transaccion.get(datasourceName);
        trx.open();
        Optional<T> item = null;
        try {
            item = dao.find(trx, id);
            trx.close();
        } catch (Exception e) {
            trx.close();
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
    public void deleteAll(Iterable<T> item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return;
        }
        Transaccion trx = Transaccion.get(datasourceName);
        trx.begin();
        try {
            for (T t : item) {
                dao.delete(trx, t);
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
    public void delete(T item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return;
        }
        Transaccion trx = Transaccion.get(datasourceName);
        trx.begin();
        try {
            dao.delete(trx, item);
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
    }

    public void deleteById(S id)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (id == null) {
            return;
        }
        Transaccion trx = Transaccion.get(datasourceName);
        trx.begin();
        try {
            dao.deletebyId(trx, id);
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
    }

    /**
     * Elimina una entidad sin lanzar exception
     *
     * @param item
     *
     */
    public void deleteSilent(T item) {
        try {
            Transaccion trx = Transaccion.get(datasourceName);
            trx.begin();
            try {
                dao.delete(trx, item);
                trx.commit();
            } catch (IllegalOrphanException | NonexistentEntityException | RollbackFailureException e) {
                trx.rollback();
            }
        } catch (Exception ex) {
            //
        }
    }

    @Override
    public List<T> findAll() {
        List<T> returnValue = null;
        try {
            Transaccion trx = Transaccion.get(datasourceName);
            trx.begin();
            try {
                returnValue = dao.findAll(trx);
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
            Transaccion trx = Transaccion.get(datasourceName);
            trx.begin();
            try {
                returnValue = dao.findAll(trx, maxResults, firstResult);
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
