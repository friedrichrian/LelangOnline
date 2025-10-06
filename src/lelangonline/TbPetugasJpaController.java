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
public class TbPetugasJpaController implements Serializable {

    public TbPetugasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TbPetugas tbPetugas) throws PreexistingEntityException, Exception {
        if (tbPetugas.getTbLelangCollection() == null) {
            tbPetugas.setTbLelangCollection(new ArrayList<TbLelang>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbLevel idLevel = tbPetugas.getIdLevel();
            if (idLevel != null) {
                idLevel = em.getReference(idLevel.getClass(), idLevel.getIdLevel());
                tbPetugas.setIdLevel(idLevel);
            }
            Collection<TbLelang> attachedTbLelangCollection = new ArrayList<TbLelang>();
            for (TbLelang tbLelangCollectionTbLelangToAttach : tbPetugas.getTbLelangCollection()) {
                tbLelangCollectionTbLelangToAttach = em.getReference(tbLelangCollectionTbLelangToAttach.getClass(), tbLelangCollectionTbLelangToAttach.getIdLelang());
                attachedTbLelangCollection.add(tbLelangCollectionTbLelangToAttach);
            }
            tbPetugas.setTbLelangCollection(attachedTbLelangCollection);
            em.persist(tbPetugas);
            if (idLevel != null) {
                idLevel.getTbPetugasCollection().add(tbPetugas);
                idLevel = em.merge(idLevel);
            }
            for (TbLelang tbLelangCollectionTbLelang : tbPetugas.getTbLelangCollection()) {
                TbPetugas oldIdPetugasOfTbLelangCollectionTbLelang = tbLelangCollectionTbLelang.getIdPetugas();
                tbLelangCollectionTbLelang.setIdPetugas(tbPetugas);
                tbLelangCollectionTbLelang = em.merge(tbLelangCollectionTbLelang);
                if (oldIdPetugasOfTbLelangCollectionTbLelang != null) {
                    oldIdPetugasOfTbLelangCollectionTbLelang.getTbLelangCollection().remove(tbLelangCollectionTbLelang);
                    oldIdPetugasOfTbLelangCollectionTbLelang = em.merge(oldIdPetugasOfTbLelangCollectionTbLelang);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTbPetugas(tbPetugas.getIdPetugas()) != null) {
                throw new PreexistingEntityException("TbPetugas " + tbPetugas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbPetugas tbPetugas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbPetugas persistentTbPetugas = em.find(TbPetugas.class, tbPetugas.getIdPetugas());
            TbLevel idLevelOld = persistentTbPetugas.getIdLevel();
            TbLevel idLevelNew = tbPetugas.getIdLevel();
            Collection<TbLelang> tbLelangCollectionOld = persistentTbPetugas.getTbLelangCollection();
            Collection<TbLelang> tbLelangCollectionNew = tbPetugas.getTbLelangCollection();
            List<String> illegalOrphanMessages = null;
            for (TbLelang tbLelangCollectionOldTbLelang : tbLelangCollectionOld) {
                if (!tbLelangCollectionNew.contains(tbLelangCollectionOldTbLelang)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TbLelang " + tbLelangCollectionOldTbLelang + " since its idPetugas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idLevelNew != null) {
                idLevelNew = em.getReference(idLevelNew.getClass(), idLevelNew.getIdLevel());
                tbPetugas.setIdLevel(idLevelNew);
            }
            Collection<TbLelang> attachedTbLelangCollectionNew = new ArrayList<TbLelang>();
            for (TbLelang tbLelangCollectionNewTbLelangToAttach : tbLelangCollectionNew) {
                tbLelangCollectionNewTbLelangToAttach = em.getReference(tbLelangCollectionNewTbLelangToAttach.getClass(), tbLelangCollectionNewTbLelangToAttach.getIdLelang());
                attachedTbLelangCollectionNew.add(tbLelangCollectionNewTbLelangToAttach);
            }
            tbLelangCollectionNew = attachedTbLelangCollectionNew;
            tbPetugas.setTbLelangCollection(tbLelangCollectionNew);
            tbPetugas = em.merge(tbPetugas);
            if (idLevelOld != null && !idLevelOld.equals(idLevelNew)) {
                idLevelOld.getTbPetugasCollection().remove(tbPetugas);
                idLevelOld = em.merge(idLevelOld);
            }
            if (idLevelNew != null && !idLevelNew.equals(idLevelOld)) {
                idLevelNew.getTbPetugasCollection().add(tbPetugas);
                idLevelNew = em.merge(idLevelNew);
            }
            for (TbLelang tbLelangCollectionNewTbLelang : tbLelangCollectionNew) {
                if (!tbLelangCollectionOld.contains(tbLelangCollectionNewTbLelang)) {
                    TbPetugas oldIdPetugasOfTbLelangCollectionNewTbLelang = tbLelangCollectionNewTbLelang.getIdPetugas();
                    tbLelangCollectionNewTbLelang.setIdPetugas(tbPetugas);
                    tbLelangCollectionNewTbLelang = em.merge(tbLelangCollectionNewTbLelang);
                    if (oldIdPetugasOfTbLelangCollectionNewTbLelang != null && !oldIdPetugasOfTbLelangCollectionNewTbLelang.equals(tbPetugas)) {
                        oldIdPetugasOfTbLelangCollectionNewTbLelang.getTbLelangCollection().remove(tbLelangCollectionNewTbLelang);
                        oldIdPetugasOfTbLelangCollectionNewTbLelang = em.merge(oldIdPetugasOfTbLelangCollectionNewTbLelang);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tbPetugas.getIdPetugas();
                if (findTbPetugas(id) == null) {
                    throw new NonexistentEntityException("The tbPetugas with id " + id + " no longer exists.");
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
            TbPetugas tbPetugas;
            try {
                tbPetugas = em.getReference(TbPetugas.class, id);
                tbPetugas.getIdPetugas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tbPetugas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TbLelang> tbLelangCollectionOrphanCheck = tbPetugas.getTbLelangCollection();
            for (TbLelang tbLelangCollectionOrphanCheckTbLelang : tbLelangCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TbPetugas (" + tbPetugas + ") cannot be destroyed since the TbLelang " + tbLelangCollectionOrphanCheckTbLelang + " in its tbLelangCollection field has a non-nullable idPetugas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TbLevel idLevel = tbPetugas.getIdLevel();
            if (idLevel != null) {
                idLevel.getTbPetugasCollection().remove(tbPetugas);
                idLevel = em.merge(idLevel);
            }
            em.remove(tbPetugas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbPetugas> findTbPetugasEntities() {
        return findTbPetugasEntities(true, -1, -1);
    }

    public List<TbPetugas> findTbPetugasEntities(int maxResults, int firstResult) {
        return findTbPetugasEntities(false, maxResults, firstResult);
    }

    private List<TbPetugas> findTbPetugasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbPetugas.class));
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

    public TbPetugas findTbPetugas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbPetugas.class, id);
        } finally {
            em.close();
        }
    }

    public int getTbPetugasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbPetugas> rt = cq.from(TbPetugas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
