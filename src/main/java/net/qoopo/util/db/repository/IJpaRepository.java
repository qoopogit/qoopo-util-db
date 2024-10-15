package net.qoopo.util.db.repository;

import java.util.List;
import java.util.Optional;

import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;

public interface IJpaRepository<T, S> {

        /**
         * Crea una lista de entidades en una sola transaccion
         *
         * @param item
         * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
         */
        public void saveAll(Iterable<T> item)
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
        public T save(T item)
                        throws NonexistentEntityException, RollbackFailureException, Exception;

        /**
         * Crea una entidad sin lanzar excepcion
         *
         * @param item
         * @return
         */
        public T saveWithoutError(T item);

        /**
         *
         * @param entityClass
         * @param id
         * @return
         */
        public Optional<T> find(S id);

        /**
         * Return all entities
         * 
         * @return
         */
        public List<T> findAll();

        /**
         * Return all entities paged
         * 
         * @param maxResults
         * @param firstResult
         * @return
         */
        public List<T> findAll(int maxResults, int firstResult);

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

        public void deleteById(S id)
                        throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception;

        /**
         * Elimina una entidad sin lanzar exception
         *
         * @param item
         *
         */
        public void deleteSilent(T item);
}
