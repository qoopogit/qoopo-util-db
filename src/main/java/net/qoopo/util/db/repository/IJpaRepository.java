package net.qoopo.util.db.repository;

import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;

public interface IJpaRepository<T> {

    /**
     * Crea una lista de entidades en una sola transaccion
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     */
    public void createAll(Iterable<T> item)
            throws NonexistentEntityException, RollbackFailureException, Exception;

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
            throws NonexistentEntityException, RollbackFailureException, Exception;

    /**
     * Crea una entidad sin lanzar excepcion
     *
     * @param item
     * @return
     */
    public T createWithoutError(T item);

    /**
     *
     * @param entityClass
     * @param id
     * @return
     */
    public T find(Long id);

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
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception;

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
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception;

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
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception;

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
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception;

    /**
     * Elimina una entidad sin lanzar exception
     *
     * @param item
     *
     */
    public void deleteSilent(T item);
}
