package net.qoopo.util.db.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;

/**
 * Controlador Jpa Genérico
 *
 * @author alberto
 * @param <T>
 */
public class JPA<T, ID> implements Serializable {

    private static Logger log = Logger.getLogger("JPA");

    public static JPA get() {
        log.warning("[!!] Instanciando JPA por get");
        return new JPA<>();
    }

    private Class<T> entityClass;
    private transient EntityManager em;
    private List<ParametroJPA> hints;
    private Parametro param;
    private Parametro storeParam;
    private int maxResults = -1;
    private int firstResult = -1;

    public JPA() {
        //
    }

    public JPA(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T create(T item) throws RollbackFailureException {
        em.persist(item);
        return item;
    }

    public T edit(T item) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
        return em.merge(item);
    }

    public void deletebyId(ID id)
            throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
        em.remove(find(id));
    }

    public void delete(T item) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
        em.remove(em.merge(item));
    }

    public List<T> findEntities() {
        return findEntities(true, -1, -1);
    }

    public List<T> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<T> findEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
            cq.select(cq.from(entityClass));
            TypedQuery<T> q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            //
        }
    }

    public Optional<T> find(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    public Long getCount() {
        try {
            CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
            Root<T> rt = cq.from(entityClass);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult());
        } finally {
            //
        }
    }

    /**
     * Configura el query con los parametros, hints , resultados maximos y
     * primeros resultados
     *
     * @param q
     */
    private Query configQuery(Query q) {
        if (param != null && param.getLista() != null && !param.getLista().isEmpty()) {
            param.getLista().forEach(p -> {
                if (p.getIndice() > 0) {
                    q.setParameter(p.getIndice(), p.getValor());
                } else {
                    q.setParameter((String) p.getParametro(), p.getValor());
                }
            });
        }
        if (hints != null && !hints.isEmpty()) {
            hints.forEach(p -> q.setHint((String) p.getParametro(), p.getValor()));
        }

        if (maxResults > -1) {
            q.setMaxResults(maxResults);
        }
        if (firstResult > -1) {
            q.setFirstResult(firstResult);
        }
        return q;
    }

    /**
     * Configura el query con los parametros, hints , resultados maximos y
     * primeros resultados
     *
     * @param q
     */
    private void configStoreProcedureQuery(StoredProcedureQuery q) {
        if (storeParam != null && storeParam.getLista() != null && !storeParam.getLista().isEmpty()) {
            storeParam.getLista().forEach(p -> q.registerStoredProcedureParameter(p.getParametro(),
                    p.getParameterClass(), p.getParameterMode()));
        }
    }

    /**
     * Ejecuta un namedQuery y devuelve <code>T</code>
     *
     * @param query
     * @return
     */
    public T ejecutarNamedQuery(String query) {
        try {
            TypedQuery<T> q = em.createNamedQuery(query, entityClass);
            configQuery(q);
            return q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Ejecuta un namedQuery y devuelve <code>Object</code>
     *
     * @param query
     * @return
     */
    public Object ejecutarNamedQueryObject(String query) {
        try {
            // return configQuery(em.createNamedQuery(query)).getSingleResult();
            Query q = em.createNamedQuery(query);
            configQuery(q);
            return q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Ejecuta un namedQuery y devuelve una lista
     *
     * @param query
     * @return
     */
    public List<T> ejecutarNamedQueryList(String query) {
        try {
            TypedQuery<T> q = em.createNamedQuery(query, entityClass);
            configQuery(q);
            return q.getResultList();
        } finally {
            //
        }
    }

    /**
     * Ejecuta un Query JPA y devuelve un objeto
     *
     * @param query
     * @return
     */
    public T ejecutarQuery(String query) {
        try {
            TypedQuery<T> q = em.createQuery(query, entityClass);
            configQuery(q);
            return q.getSingleResult();
        } finally {
            //
        }
    }

    /**
     * Ejecuta un Query JPA y devuelve una lista
     *
     * @param query
     * @return
     */
    public List<T> ejecutarQueryList(String query) {
        try {
            TypedQuery<T> q = em.createQuery(query, entityClass);
            configQuery(q);
            return q.getResultList();
        } finally {
            //
        }
    }

    /**
     * Ejecuta un query nativo de la bdd y devuelve un objeto
     *
     * @param query
     * @return
     */
    public Object ejecutarNativeQuery(String query) {
        try {
            Query q = em.createNativeQuery(query);
            configQuery(q);
            return q.getSingleResult();
        } finally {
            //
        }
    }

    /**
     * Ejecuta un query nativo de la bdd y devuelve una lista
     *
     * @param query
     * @return
     */
    public List<Object> ejecutarNativeQueryList(String query) {
        try {
            Query q = em.createNativeQuery(query);
            configQuery(q);
            return q.getResultList();
        } finally {
            //
        }
    }

    /**
     * Ejecuta un query nativo de actualizacion (UPDATE)
     *
     * @param query
     */
    public void ejecutarNativeQueryUpdate(String query) {
        try {
            Query q = em.createNativeQuery(query);
            configQuery(q);
            q.executeUpdate();
        } finally {
            //
        }
    }

    /**
     * Ejecuta un Query JPA y devuelve un objeto
     *
     * @param query
     * @return
     */
    public StoredProcedureQuery setStoreProcedure(String query) {
        try {
            StoredProcedureQuery q = em.createStoredProcedureQuery(query);
            configStoreProcedureQuery(q);
            configQuery(q);
            return q;
        } finally {
            //
        }
    }

    /**
     * Ejecuta un Query JPA y devuelve un objeto
     *
     * @param query
     * @return
     */
    public Object ejecutarStoreProcedure(String query) {
        try {
            StoredProcedureQuery q = em.createStoredProcedureQuery(query);
            configStoreProcedureQuery(q);
            configQuery(q);
            q.execute();
            return q.getSingleResult();
        } finally {
            //
        }
    }

    /**
     * Ejecuta un Query JPA y devuelve una lista
     *
     * @param query
     * @return
     */
    public List<T> ejecutarStoreProcedureList(String query) {
        try {
            StoredProcedureQuery q = em.createStoredProcedureQuery(query);
            configStoreProcedureQuery(q);
            configQuery(q);
            q.execute();
            return q.getResultList();
        } finally {
            //
        }
    }

    public List<ParametroJPA> getHints() {
        return hints;
    }

    public JPA<T, ID> setHints(List<ParametroJPA> hints) {
        this.hints = hints;
        return this;
    }

    public JPA<T, ID> setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    public JPA<T, ID> setEm(EntityManager em) {
        this.em = em;
        return this;
    }

    public JPA<T, ID> setParam(Parametro param) {
        this.param = param;
        return this;
    }

    public JPA<T, ID> setStoreParam(Parametro param) {
        this.storeParam = param;
        return this;
    }

    public JPA<T, ID> setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public JPA<T, ID> setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

}
