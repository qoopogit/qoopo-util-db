package net.qoopo.util.db.dao;

import java.util.List;
import java.util.Optional;

import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;

/**
 * Clase que permite realiza las operaciones basicas CRUD sobre cualquier
 * entidad de la base de datos
 *
 * @author alberto
 */
public interface ICrudDAO<T, ID> {

        public T create(T item) throws Exception, RollbackFailureException;

        public T edit(T item)
                        throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException;

        public void deletebyId(ID id)
                        throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException;

        public void delete(T item)
                        throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException;

        public Optional<T> find(ID id);

        public List<T> findAll();

        public List<T> findAll(int maxResults, int firstResult);

}
