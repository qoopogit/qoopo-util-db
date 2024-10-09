package net.qoopo.util.db.dao;

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
public interface ICrudDAO<T> {

    public T create(Transaccion transaccion, T item) throws Exception, RollbackFailureException;

    public T edit(Transaccion transaccion, T item)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException;

    public void delete(Transaccion transaccion, T item)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException;

    public T find(Transaccion transaccion, Long id);

}
