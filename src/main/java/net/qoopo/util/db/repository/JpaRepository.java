package net.qoopo.util.db.repository;

import java.util.logging.Logger;

import net.qoopo.util.db.dao.CrudDAO;
import net.qoopo.util.db.jpa.Transaccion;
import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;

public class JpaRepository<T> implements IJpaRepository<T> {
    public static final Logger log = Logger.getLogger("qoopo-framework-qoopojparepository");

    private CrudDAO<T> dao;
    private String datasourceName;

    public JpaRepository(Class<T> entityClass, String datasourceName) {
        dao = new CrudDAO<>(entityClass);
    }

    /**
     * Crea una lista de entidades en una sola transaccion
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     */
    public void createAll(Iterable<T> item)
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
    public T create(T item)
            throws NonexistentEntityException, RollbackFailureException, Exception {
        if (item == null) {
            return null;
        }
        Transaccion trx = Transaccion.get(datasourceName);
        trx.begin();
        try {
            dao.create(trx, item);
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
    public T createWithoutError(T item) {
        try {
            if (item == null) {
                return null;
            }
            Transaccion trx = Transaccion.get(datasourceName);
            trx.begin();
            try {
                dao.create(trx, item);
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
    public T find(Long id) {
        Transaccion trx = Transaccion.get(datasourceName);
        trx.abrir();
        T item = null;
        try {
            item = (T) dao.find(trx, id);
            trx.cerrar();
        } catch (Exception e) {
            trx.cerrar();
        }
        return item;
    }

    /**
     * Edita una lista de entidades en una sola transaccion
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.IllegalOrphanException
     */
    public void editAll(Iterable<T> item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return;
        }
        Transaccion trx = Transaccion.get(datasourceName);
        trx.begin();
        try {
            for (T t : item) {
                dao.edit(trx, t);
            }
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
    }

    /**
     * Edita una conciliación
     *
     * @param item
     * @return
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.IllegalOrphanException
     */
    public T edit(T item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return null;
        }
        Transaccion trx = Transaccion.get(datasourceName);
        trx.begin();
        try {
            item = (T) dao.edit(trx, item);
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
}
