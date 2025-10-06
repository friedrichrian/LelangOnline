/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lelangonline;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import lelangonline.exceptions.IllegalOrphanException;
import lelangonline.exceptions.NonexistentEntityException;
import lelangonline.exceptions.PreexistingEntityException;

/**
 *
 * @author fikri
 */
public class TbBarangJpaController implements Serializable {

    public TbBarangJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TbBarang tbBarang) throws PreexistingEntityException, Exception {
        if (tbBarang.getHistoryLelangCollection() == null) {
            tbBarang.setHistoryLelangCollection(new ArrayList<HistoryLelang>());
        }
        if (tbBarang.getTbLelangCollection() == null) {
            tbBarang.setTbLelangCollection(new ArrayList<TbLelang>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<HistoryLelang> attachedHistoryLelangCollection = new ArrayList<HistoryLelang>();
            for (HistoryLelang historyLelangCollectionHistoryLelangToAttach : tbBarang.getHistoryLelangCollection()) {
                historyLelangCollectionHistoryLelangToAttach = em.getReference(historyLelangCollectionHistoryLelangToAttach.getClass(), historyLelangCollectionHistoryLelangToAttach.getIdHistory());
                attachedHistoryLelangCollection.add(historyLelangCollectionHistoryLelangToAttach);
            }
            tbBarang.setHistoryLelangCollection(attachedHistoryLelangCollection);
            Collection<TbLelang> attachedTbLelangCollection = new ArrayList<TbLelang>();
            for (TbLelang tbLelangCollectionTbLelangToAttach : tbBarang.getTbLelangCollection()) {
                tbLelangCollectionTbLelangToAttach = em.getReference(tbLelangCollectionTbLelangToAttach.getClass(), tbLelangCollectionTbLelangToAttach.getIdLelang());
                attachedTbLelangCollection.add(tbLelangCollectionTbLelangToAttach);
            }
            tbBarang.setTbLelangCollection(attachedTbLelangCollection);
            em.persist(tbBarang);
            for (HistoryLelang historyLelangCollectionHistoryLelang : tbBarang.getHistoryLelangCollection()) {
                TbBarang oldIdBarangOfHistoryLelangCollectionHistoryLelang = historyLelangCollectionHistoryLelang.getIdBarang();
                historyLelangCollectionHistoryLelang.setIdBarang(tbBarang);
                historyLelangCollectionHistoryLelang = em.merge(historyLelangCollectionHistoryLelang);
                if (oldIdBarangOfHistoryLelangCollectionHistoryLelang != null) {
                    oldIdBarangOfHistoryLelangCollectionHistoryLelang.getHistoryLelangCollection().remove(historyLelangCollectionHistoryLelang);
                    oldIdBarangOfHistoryLelangCollectionHistoryLelang = em.merge(oldIdBarangOfHistoryLelangCollectionHistoryLelang);
                }
            }
            for (TbLelang tbLelangCollectionTbLelang : tbBarang.getTbLelangCollection()) {
                TbBarang oldIdBarangOfTbLelangCollectionTbLelang = tbLelangCollectionTbLelang.getIdBarang();
                tbLelangCollectionTbLelang.setIdBarang(tbBarang);
                tbLelangCollectionTbLelang = em.merge(tbLelangCollectionTbLelang);
                if (oldIdBarangOfTbLelangCollectionTbLelang != null) {
                    oldIdBarangOfTbLelangCollectionTbLelang.getTbLelangCollection().remove(tbLelangCollectionTbLelang);
                    oldIdBarangOfTbLelangCollectionTbLelang = em.merge(oldIdBarangOfTbLelangCollectionTbLelang);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTbBarang(tbBarang.getIdBarang()) != null) {
                throw new PreexistingEntityException("TbBarang " + tbBarang + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbBarang tbBarang) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBarang persistentTbBarang = em.find(TbBarang.class, tbBarang.getIdBarang());
            Collection<HistoryLelang> historyLelangCollectionOld = persistentTbBarang.getHistoryLelangCollection();
            Collection<HistoryLelang> historyLelangCollectionNew = tbBarang.getHistoryLelangCollection();
            Collection<TbLelang> tbLelangCollectionOld = persistentTbBarang.getTbLelangCollection();
            Collection<TbLelang> tbLelangCollectionNew = tbBarang.getTbLelangCollection();
            List<String> illegalOrphanMessages = null;
            for (HistoryLelang historyLelangCollectionOldHistoryLelang : historyLelangCollectionOld) {
                if (!historyLelangCollectionNew.contains(historyLelangCollectionOldHistoryLelang)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistoryLelang " + historyLelangCollectionOldHistoryLelang + " since its idBarang field is not nullable.");
                }
            }
            for (TbLelang tbLelangCollectionOldTbLelang : tbLelangCollectionOld) {
                if (!tbLelangCollectionNew.contains(tbLelangCollectionOldTbLelang)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TbLelang " + tbLelangCollectionOldTbLelang + " since its idBarang field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<HistoryLelang> attachedHistoryLelangCollectionNew = new ArrayList<HistoryLelang>();
            for (HistoryLelang historyLelangCollectionNewHistoryLelangToAttach : historyLelangCollectionNew) {
                historyLelangCollectionNewHistoryLelangToAttach = em.getReference(historyLelangCollectionNewHistoryLelangToAttach.getClass(), historyLelangCollectionNewHistoryLelangToAttach.getIdHistory());
                attachedHistoryLelangCollectionNew.add(historyLelangCollectionNewHistoryLelangToAttach);
            }
            historyLelangCollectionNew = attachedHistoryLelangCollectionNew;
            tbBarang.setHistoryLelangCollection(historyLelangCollectionNew);
            Collection<TbLelang> attachedTbLelangCollectionNew = new ArrayList<TbLelang>();
            for (TbLelang tbLelangCollectionNewTbLelangToAttach : tbLelangCollectionNew) {
                tbLelangCollectionNewTbLelangToAttach = em.getReference(tbLelangCollectionNewTbLelangToAttach.getClass(), tbLelangCollectionNewTbLelangToAttach.getIdLelang());
                attachedTbLelangCollectionNew.add(tbLelangCollectionNewTbLelangToAttach);
            }
            tbLelangCollectionNew = attachedTbLelangCollectionNew;
            tbBarang.setTbLelangCollection(tbLelangCollectionNew);
            tbBarang = em.merge(tbBarang);
            for (HistoryLelang historyLelangCollectionNewHistoryLelang : historyLelangCollectionNew) {
                if (!historyLelangCollectionOld.contains(historyLelangCollectionNewHistoryLelang)) {
                    TbBarang oldIdBarangOfHistoryLelangCollectionNewHistoryLelang = historyLelangCollectionNewHistoryLelang.getIdBarang();
                    historyLelangCollectionNewHistoryLelang.setIdBarang(tbBarang);
                    historyLelangCollectionNewHistoryLelang = em.merge(historyLelangCollectionNewHistoryLelang);
                    if (oldIdBarangOfHistoryLelangCollectionNewHistoryLelang != null && !oldIdBarangOfHistoryLelangCollectionNewHistoryLelang.equals(tbBarang)) {
                        oldIdBarangOfHistoryLelangCollectionNewHistoryLelang.getHistoryLelangCollection().remove(historyLelangCollectionNewHistoryLelang);
                        oldIdBarangOfHistoryLelangCollectionNewHistoryLelang = em.merge(oldIdBarangOfHistoryLelangCollectionNewHistoryLelang);
                    }
                }
            }
            for (TbLelang tbLelangCollectionNewTbLelang : tbLelangCollectionNew) {
                if (!tbLelangCollectionOld.contains(tbLelangCollectionNewTbLelang)) {
                    TbBarang oldIdBarangOfTbLelangCollectionNewTbLelang = tbLelangCollectionNewTbLelang.getIdBarang();
                    tbLelangCollectionNewTbLelang.setIdBarang(tbBarang);
                    tbLelangCollectionNewTbLelang = em.merge(tbLelangCollectionNewTbLelang);
                    if (oldIdBarangOfTbLelangCollectionNewTbLelang != null && !oldIdBarangOfTbLelangCollectionNewTbLelang.equals(tbBarang)) {
                        oldIdBarangOfTbLelangCollectionNewTbLelang.getTbLelangCollection().remove(tbLelangCollectionNewTbLelang);
                        oldIdBarangOfTbLelangCollectionNewTbLelang = em.merge(oldIdBarangOfTbLelangCollectionNewTbLelang);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tbBarang.getIdBarang();
                if (findTbBarang(id) == null) {
                    throw new NonexistentEntityException("The tbBarang with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBarang tbBarang;
            try {
                tbBarang = em.getReference(TbBarang.class, id);
                tbBarang.getIdBarang();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tbBarang with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<HistoryLelang> historyLelangCollectionOrphanCheck = tbBarang.getHistoryLelangCollection();
            for (HistoryLelang historyLelangCollectionOrphanCheckHistoryLelang : historyLelangCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TbBarang (" + tbBarang + ") cannot be destroyed since the HistoryLelang " + historyLelangCollectionOrphanCheckHistoryLelang + " in its historyLelangCollection field has a non-nullable idBarang field.");
            }
            Collection<TbLelang> tbLelangCollectionOrphanCheck = tbBarang.getTbLelangCollection();
            for (TbLelang tbLelangCollectionOrphanCheckTbLelang : tbLelangCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TbBarang (" + tbBarang + ") cannot be destroyed since the TbLelang " + tbLelangCollectionOrphanCheckTbLelang + " in its tbLelangCollection field has a non-nullable idBarang field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tbBarang);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbBarang> findTbBarangEntities() {
        return findTbBarangEntities(true, -1, -1);
    }

    public List<TbBarang> findTbBarangEntities(int maxResults, int firstResult) {
        return findTbBarangEntities(false, maxResults, firstResult);
    }

    private List<TbBarang> findTbBarangEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbBarang.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TbBarang findTbBarang(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbBarang.class, id);
        } finally {
            em.close();
        }
    }

    public int getTbBarangCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbBarang> rt = cq.from(TbBarang.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
