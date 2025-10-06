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
public class TbLevelJpaController implements Serializable {

    public TbLevelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TbLevel tbLevel) throws PreexistingEntityException, Exception {
        if (tbLevel.getTbPetugasCollection() == null) {
            tbLevel.setTbPetugasCollection(new ArrayList<TbPetugas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TbPetugas> attachedTbPetugasCollection = new ArrayList<TbPetugas>();
            for (TbPetugas tbPetugasCollectionTbPetugasToAttach : tbLevel.getTbPetugasCollection()) {
                tbPetugasCollectionTbPetugasToAttach = em.getReference(tbPetugasCollectionTbPetugasToAttach.getClass(), tbPetugasCollectionTbPetugasToAttach.getIdPetugas());
                attachedTbPetugasCollection.add(tbPetugasCollectionTbPetugasToAttach);
            }
            tbLevel.setTbPetugasCollection(attachedTbPetugasCollection);
            em.persist(tbLevel);
            for (TbPetugas tbPetugasCollectionTbPetugas : tbLevel.getTbPetugasCollection()) {
                TbLevel oldIdLevelOfTbPetugasCollectionTbPetugas = tbPetugasCollectionTbPetugas.getIdLevel();
                tbPetugasCollectionTbPetugas.setIdLevel(tbLevel);
                tbPetugasCollectionTbPetugas = em.merge(tbPetugasCollectionTbPetugas);
                if (oldIdLevelOfTbPetugasCollectionTbPetugas != null) {
                    oldIdLevelOfTbPetugasCollectionTbPetugas.getTbPetugasCollection().remove(tbPetugasCollectionTbPetugas);
                    oldIdLevelOfTbPetugasCollectionTbPetugas = em.merge(oldIdLevelOfTbPetugasCollectionTbPetugas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTbLevel(tbLevel.getIdLevel()) != null) {
                throw new PreexistingEntityException("TbLevel " + tbLevel + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbLevel tbLevel) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbLevel persistentTbLevel = em.find(TbLevel.class, tbLevel.getIdLevel());
            Collection<TbPetugas> tbPetugasCollectionOld = persistentTbLevel.getTbPetugasCollection();
            Collection<TbPetugas> tbPetugasCollectionNew = tbLevel.getTbPetugasCollection();
            List<String> illegalOrphanMessages = null;
            for (TbPetugas tbPetugasCollectionOldTbPetugas : tbPetugasCollectionOld) {
                if (!tbPetugasCollectionNew.contains(tbPetugasCollectionOldTbPetugas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TbPetugas " + tbPetugasCollectionOldTbPetugas + " since its idLevel field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TbPetugas> attachedTbPetugasCollectionNew = new ArrayList<TbPetugas>();
            for (TbPetugas tbPetugasCollectionNewTbPetugasToAttach : tbPetugasCollectionNew) {
                tbPetugasCollectionNewTbPetugasToAttach = em.getReference(tbPetugasCollectionNewTbPetugasToAttach.getClass(), tbPetugasCollectionNewTbPetugasToAttach.getIdPetugas());
                attachedTbPetugasCollectionNew.add(tbPetugasCollectionNewTbPetugasToAttach);
            }
            tbPetugasCollectionNew = attachedTbPetugasCollectionNew;
            tbLevel.setTbPetugasCollection(tbPetugasCollectionNew);
            tbLevel = em.merge(tbLevel);
            for (TbPetugas tbPetugasCollectionNewTbPetugas : tbPetugasCollectionNew) {
                if (!tbPetugasCollectionOld.contains(tbPetugasCollectionNewTbPetugas)) {
                    TbLevel oldIdLevelOfTbPetugasCollectionNewTbPetugas = tbPetugasCollectionNewTbPetugas.getIdLevel();
                    tbPetugasCollectionNewTbPetugas.setIdLevel(tbLevel);
                    tbPetugasCollectionNewTbPetugas = em.merge(tbPetugasCollectionNewTbPetugas);
                    if (oldIdLevelOfTbPetugasCollectionNewTbPetugas != null && !oldIdLevelOfTbPetugasCollectionNewTbPetugas.equals(tbLevel)) {
                        oldIdLevelOfTbPetugasCollectionNewTbPetugas.getTbPetugasCollection().remove(tbPetugasCollectionNewTbPetugas);
                        oldIdLevelOfTbPetugasCollectionNewTbPetugas = em.merge(oldIdLevelOfTbPetugasCollectionNewTbPetugas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tbLevel.getIdLevel();
                if (findTbLevel(id) == null) {
                    throw new NonexistentEntityException("The tbLevel with id " + id + " no longer exists.");
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
            TbLevel tbLevel;
            try {
                tbLevel = em.getReference(TbLevel.class, id);
                tbLevel.getIdLevel();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tbLevel with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TbPetugas> tbPetugasCollectionOrphanCheck = tbLevel.getTbPetugasCollection();
            for (TbPetugas tbPetugasCollectionOrphanCheckTbPetugas : tbPetugasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TbLevel (" + tbLevel + ") cannot be destroyed since the TbPetugas " + tbPetugasCollectionOrphanCheckTbPetugas + " in its tbPetugasCollection field has a non-nullable idLevel field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tbLevel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbLevel> findTbLevelEntities() {
        return findTbLevelEntities(true, -1, -1);
    }

    public List<TbLevel> findTbLevelEntities(int maxResults, int firstResult) {
        return findTbLevelEntities(false, maxResults, firstResult);
    }

    private List<TbLevel> findTbLevelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbLevel.class));
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

    public TbLevel findTbLevel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbLevel.class, id);
        } finally {
            em.close();
        }
    }

    public int getTbLevelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbLevel> rt = cq.from(TbLevel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
