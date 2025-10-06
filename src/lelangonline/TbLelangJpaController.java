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
public class TbLelangJpaController implements Serializable {

    public TbLelangJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TbLelang tbLelang) throws PreexistingEntityException, Exception {
        if (tbLelang.getHistoryLelangCollection() == null) {
            tbLelang.setHistoryLelangCollection(new ArrayList<HistoryLelang>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBarang idBarang = tbLelang.getIdBarang();
            if (idBarang != null) {
                idBarang = em.getReference(idBarang.getClass(), idBarang.getIdBarang());
                tbLelang.setIdBarang(idBarang);
            }
            TbMasyarakat idUser = tbLelang.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getIdUser());
                tbLelang.setIdUser(idUser);
            }
            TbPetugas idPetugas = tbLelang.getIdPetugas();
            if (idPetugas != null) {
                idPetugas = em.getReference(idPetugas.getClass(), idPetugas.getIdPetugas());
                tbLelang.setIdPetugas(idPetugas);
            }
            Collection<HistoryLelang> attachedHistoryLelangCollection = new ArrayList<HistoryLelang>();
            for (HistoryLelang historyLelangCollectionHistoryLelangToAttach : tbLelang.getHistoryLelangCollection()) {
                historyLelangCollectionHistoryLelangToAttach = em.getReference(historyLelangCollectionHistoryLelangToAttach.getClass(), historyLelangCollectionHistoryLelangToAttach.getIdHistory());
                attachedHistoryLelangCollection.add(historyLelangCollectionHistoryLelangToAttach);
            }
            tbLelang.setHistoryLelangCollection(attachedHistoryLelangCollection);
            em.persist(tbLelang);
            if (idBarang != null) {
                idBarang.getTbLelangCollection().add(tbLelang);
                idBarang = em.merge(idBarang);
            }
            if (idUser != null) {
                idUser.getTbLelangCollection().add(tbLelang);
                idUser = em.merge(idUser);
            }
            if (idPetugas != null) {
                idPetugas.getTbLelangCollection().add(tbLelang);
                idPetugas = em.merge(idPetugas);
            }
            for (HistoryLelang historyLelangCollectionHistoryLelang : tbLelang.getHistoryLelangCollection()) {
                TbLelang oldIdLelangOfHistoryLelangCollectionHistoryLelang = historyLelangCollectionHistoryLelang.getIdLelang();
                historyLelangCollectionHistoryLelang.setIdLelang(tbLelang);
                historyLelangCollectionHistoryLelang = em.merge(historyLelangCollectionHistoryLelang);
                if (oldIdLelangOfHistoryLelangCollectionHistoryLelang != null) {
                    oldIdLelangOfHistoryLelangCollectionHistoryLelang.getHistoryLelangCollection().remove(historyLelangCollectionHistoryLelang);
                    oldIdLelangOfHistoryLelangCollectionHistoryLelang = em.merge(oldIdLelangOfHistoryLelangCollectionHistoryLelang);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTbLelang(tbLelang.getIdLelang()) != null) {
                throw new PreexistingEntityException("TbLelang " + tbLelang + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbLelang tbLelang) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbLelang persistentTbLelang = em.find(TbLelang.class, tbLelang.getIdLelang());
            TbBarang idBarangOld = persistentTbLelang.getIdBarang();
            TbBarang idBarangNew = tbLelang.getIdBarang();
            TbMasyarakat idUserOld = persistentTbLelang.getIdUser();
            TbMasyarakat idUserNew = tbLelang.getIdUser();
            TbPetugas idPetugasOld = persistentTbLelang.getIdPetugas();
            TbPetugas idPetugasNew = tbLelang.getIdPetugas();
            Collection<HistoryLelang> historyLelangCollectionOld = persistentTbLelang.getHistoryLelangCollection();
            Collection<HistoryLelang> historyLelangCollectionNew = tbLelang.getHistoryLelangCollection();
            List<String> illegalOrphanMessages = null;
            for (HistoryLelang historyLelangCollectionOldHistoryLelang : historyLelangCollectionOld) {
                if (!historyLelangCollectionNew.contains(historyLelangCollectionOldHistoryLelang)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistoryLelang " + historyLelangCollectionOldHistoryLelang + " since its idLelang field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idBarangNew != null) {
                idBarangNew = em.getReference(idBarangNew.getClass(), idBarangNew.getIdBarang());
                tbLelang.setIdBarang(idBarangNew);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getIdUser());
                tbLelang.setIdUser(idUserNew);
            }
            if (idPetugasNew != null) {
                idPetugasNew = em.getReference(idPetugasNew.getClass(), idPetugasNew.getIdPetugas());
                tbLelang.setIdPetugas(idPetugasNew);
            }
            Collection<HistoryLelang> attachedHistoryLelangCollectionNew = new ArrayList<HistoryLelang>();
            for (HistoryLelang historyLelangCollectionNewHistoryLelangToAttach : historyLelangCollectionNew) {
                historyLelangCollectionNewHistoryLelangToAttach = em.getReference(historyLelangCollectionNewHistoryLelangToAttach.getClass(), historyLelangCollectionNewHistoryLelangToAttach.getIdHistory());
                attachedHistoryLelangCollectionNew.add(historyLelangCollectionNewHistoryLelangToAttach);
            }
            historyLelangCollectionNew = attachedHistoryLelangCollectionNew;
            tbLelang.setHistoryLelangCollection(historyLelangCollectionNew);
            tbLelang = em.merge(tbLelang);
            if (idBarangOld != null && !idBarangOld.equals(idBarangNew)) {
                idBarangOld.getTbLelangCollection().remove(tbLelang);
                idBarangOld = em.merge(idBarangOld);
            }
            if (idBarangNew != null && !idBarangNew.equals(idBarangOld)) {
                idBarangNew.getTbLelangCollection().add(tbLelang);
                idBarangNew = em.merge(idBarangNew);
            }
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getTbLelangCollection().remove(tbLelang);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getTbLelangCollection().add(tbLelang);
                idUserNew = em.merge(idUserNew);
            }
            if (idPetugasOld != null && !idPetugasOld.equals(idPetugasNew)) {
                idPetugasOld.getTbLelangCollection().remove(tbLelang);
                idPetugasOld = em.merge(idPetugasOld);
            }
            if (idPetugasNew != null && !idPetugasNew.equals(idPetugasOld)) {
                idPetugasNew.getTbLelangCollection().add(tbLelang);
                idPetugasNew = em.merge(idPetugasNew);
            }
            for (HistoryLelang historyLelangCollectionNewHistoryLelang : historyLelangCollectionNew) {
                if (!historyLelangCollectionOld.contains(historyLelangCollectionNewHistoryLelang)) {
                    TbLelang oldIdLelangOfHistoryLelangCollectionNewHistoryLelang = historyLelangCollectionNewHistoryLelang.getIdLelang();
                    historyLelangCollectionNewHistoryLelang.setIdLelang(tbLelang);
                    historyLelangCollectionNewHistoryLelang = em.merge(historyLelangCollectionNewHistoryLelang);
                    if (oldIdLelangOfHistoryLelangCollectionNewHistoryLelang != null && !oldIdLelangOfHistoryLelangCollectionNewHistoryLelang.equals(tbLelang)) {
                        oldIdLelangOfHistoryLelangCollectionNewHistoryLelang.getHistoryLelangCollection().remove(historyLelangCollectionNewHistoryLelang);
                        oldIdLelangOfHistoryLelangCollectionNewHistoryLelang = em.merge(oldIdLelangOfHistoryLelangCollectionNewHistoryLelang);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tbLelang.getIdLelang();
                if (findTbLelang(id) == null) {
                    throw new NonexistentEntityException("The tbLelang with id " + id + " no longer exists.");
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
            TbLelang tbLelang;
            try {
                tbLelang = em.getReference(TbLelang.class, id);
                tbLelang.getIdLelang();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tbLelang with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<HistoryLelang> historyLelangCollectionOrphanCheck = tbLelang.getHistoryLelangCollection();
            for (HistoryLelang historyLelangCollectionOrphanCheckHistoryLelang : historyLelangCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TbLelang (" + tbLelang + ") cannot be destroyed since the HistoryLelang " + historyLelangCollectionOrphanCheckHistoryLelang + " in its historyLelangCollection field has a non-nullable idLelang field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TbBarang idBarang = tbLelang.getIdBarang();
            if (idBarang != null) {
                idBarang.getTbLelangCollection().remove(tbLelang);
                idBarang = em.merge(idBarang);
            }
            TbMasyarakat idUser = tbLelang.getIdUser();
            if (idUser != null) {
                idUser.getTbLelangCollection().remove(tbLelang);
                idUser = em.merge(idUser);
            }
            TbPetugas idPetugas = tbLelang.getIdPetugas();
            if (idPetugas != null) {
                idPetugas.getTbLelangCollection().remove(tbLelang);
                idPetugas = em.merge(idPetugas);
            }
            em.remove(tbLelang);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbLelang> findTbLelangEntities() {
        return findTbLelangEntities(true, -1, -1);
    }

    public List<TbLelang> findTbLelangEntities(int maxResults, int firstResult) {
        return findTbLelangEntities(false, maxResults, firstResult);
    }

    private List<TbLelang> findTbLelangEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbLelang.class));
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

    public TbLelang findTbLelang(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbLelang.class, id);
        } finally {
            em.close();
        }
    }

    public int getTbLelangCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbLelang> rt = cq.from(TbLelang.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
