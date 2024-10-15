package net.qoopo.util.db.dao;

import java.util.List;
import java.util.Optional;

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
public interface ICrudDAO<T, S> {

        public T create(Transaccion transaccion, T item) throws Exception, RollbackFailureException;

        public T edit(Transaccion transaccion, T item)
                        throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException;

        public void delete(Transaccion transaccion, T item)
                        throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException;

        public Optional<T> find(Transaccion transaccion, S id);

        public List<T> findAll(Transaccion transaccion);

        public List<T> findAll(Transaccion transaccion, int maxResults, int firstResult);

}
