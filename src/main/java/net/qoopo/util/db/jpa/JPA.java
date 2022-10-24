package net.qoopo.util.db.jpa;

import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Controlador Jpa Genérico
 *
 * @author alberto
 * @param <T>
 */
public class JPA<T> implements Serializable {

    public static JPA get() {
        return new JPA<>();
    }

    private Class<T> clase;
    private transient EntityManager em;
    private List<ParametroJPA> hints;
    private Parametro param;
    private Parametro storeParam;
    private int maxResults = -1;
    private int firstResult = -1;

    private JPA() {
        //
    }

    public T crear(T item) throws RollbackFailureException {
        em.persist(item);
        return item;
    }

    public T editar(T item) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
        item = em.merge(item);
        return item;
    }

    public void eliminar(T item) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
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
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(clase));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            //
        }
    }

    public T buscar(Object id) {
        return em.find(clase, id);
    }

    public int getCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<T> rt = cq.from(clase);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
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
    private void configQuery(Query q) {
        if (param != null && param.getLista() != null && !param.getLista().isEmpty()) {
            param.getLista().forEach(p -> q.setParameter((String) p.getParametro(), p.getValor()));
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
    }

    /**
     * Configura el query con los parametros, hints , resultados maximos y
     * primeros resultados
     *
     * @param q
     */
    private void configStoreProcedureQuery(StoredProcedureQuery q) {
        if (storeParam != null && storeParam.getLista() != null && !storeParam.getLista().isEmpty()) {
            storeParam.getLista().forEach(p -> q.registerStoredProcedureParameter(p.getParametro(), p.getParameterClass(), p.getParameterMode()));
        }
    }

    /**
     * Ejecuta un namedQuery y devuelve un Objeto
     *
     * @param query
     * @return
     */
    public Object ejecutarNamedQuery(String query) {
        try {
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
    public List<Object> ejecutarNamedQueryList(String query) {
        try {
            Query q = em.createNamedQuery(query);
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
    public Object ejecutarQuery(String query) {
        try {
            Query q = em.createQuery(query);
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
    public List<Object> ejecutarQueryList(String query) {
        try {
            Query q = em.createQuery(query);
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
    public List<Object> ejecutarStoreProcedureList(String query) {
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

    public JPA setHints(List<ParametroJPA> hints) {
        this.hints = hints;
        return this;
    }

    public JPA setClase(Class<T> entityClass) {
        this.clase = entityClass;
        return this;
    }

    public JPA setEm(EntityManager em) {
        this.em = em;
        return this;
    }

    public JPA setParam(Parametro param) {
        this.param = param;
        return this;
    }

    public JPA setStoreParam(Parametro param) {
        this.storeParam = param;
        return this;
    }

    public JPA setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public JPA setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

}
