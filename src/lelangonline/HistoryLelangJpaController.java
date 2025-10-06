/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lelangonline;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lelangonline.exceptions.NonexistentEntityException;
import lelangonline.exceptions.PreexistingEntityException;

/**
 *
 * @author fikri
 */
public class HistoryLelangJpaController implements Serializable {

    public HistoryLelangJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistoryLelang historyLelang) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBarang idBarang = historyLelang.getIdBarang();
            if (idBarang != null) {
                idBarang = em.getReference(idBarang.getClass(), idBarang.getIdBarang());
                historyLelang.setIdBarang(idBarang);
            }
            TbMasyarakat idUser = historyLelang.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getIdUser());
                historyLelang.setIdUser(idUser);
            }
            TbLelang idLelang = historyLelang.getIdLelang();
            if (idLelang != null) {
                idLelang = em.getReference(idLelang.getClass(), idLelang.getIdLelang());
                historyLelang.setIdLelang(idLelang);
            }
            em.persist(historyLelang);
            if (idBarang != null) {
                idBarang.getHistoryLelangCollection().add(historyLelang);
                idBarang = em.merge(idBarang);
            }
            if (idUser != null) {
                idUser.getHistoryLelangCollection().add(historyLelang);
                idUser = em.merge(idUser);
            }
            if (idLelang != null) {
                idLelang.getHistoryLelangCollection().add(historyLelang);
                idLelang = em.merge(idLelang);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHistoryLelang(historyLelang.getIdHistory()) != null) {
                throw new PreexistingEntityException("HistoryLelang " + historyLelang + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistoryLelang historyLelang) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistoryLelang persistentHistoryLelang = em.find(HistoryLelang.class, historyLelang.getIdHistory());
            TbBarang idBarangOld = persistentHistoryLelang.getIdBarang();
            TbBarang idBarangNew = historyLelang.getIdBarang();
            TbMasyarakat idUserOld = persistentHistoryLelang.getIdUser();
            TbMasyarakat idUserNew = historyLelang.getIdUser();
            TbLelang idLelangOld = persistentHistoryLelang.getIdLelang();
            TbLelang idLelangNew = historyLelang.getIdLelang();
            if (idBarangNew != null) {
                idBarangNew = em.getReference(idBarangNew.getClass(), idBarangNew.getIdBarang());
                historyLelang.setIdBarang(idBarangNew);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getIdUser());
                historyLelang.setIdUser(idUserNew);
            }
            if (idLelangNew != null) {
                idLelangNew = em.getReference(idLelangNew.getClass(), idLelangNew.getIdLelang());
                historyLelang.setIdLelang(idLelangNew);
            }
            historyLelang = em.merge(historyLelang);
            if (idBarangOld != null && !idBarangOld.equals(idBarangNew)) {
                idBarangOld.getHistoryLelangCollection().remove(historyLelang);
                idBarangOld = em.merge(idBarangOld);
            }
            if (idBarangNew != null && !idBarangNew.equals(idBarangOld)) {
                idBarangNew.getHistoryLelangCollection().add(historyLelang);
                idBarangNew = em.merge(idBarangNew);
            }
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getHistoryLelangCollection().remove(historyLelang);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getHistoryLelangCollection().add(historyLelang);
                idUserNew = em.merge(idUserNew);
            }
            if (idLelangOld != null && !idLelangOld.equals(idLelangNew)) {
                idLelangOld.getHistoryLelangCollection().remove(historyLelang);
                idLelangOld = em.merge(idLelangOld);
            }
            if (idLelangNew != null && !idLelangNew.equals(idLelangOld)) {
                idLelangNew.getHistoryLelangCollection().add(historyLelang);
                idLelangNew = em.merge(idLelangNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historyLelang.getIdHistory();
                if (findHistoryLelang(id) == null) {
                    throw new NonexistentEntityException("The historyLelang with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistoryLelang historyLelang;
            try {
                historyLelang = em.getReference(HistoryLelang.class, id);
                historyLelang.getIdHistory();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historyLelang with id " + id + " no longer exists.", enfe);
            }
            TbBarang idBarang = historyLelang.getIdBarang();
            if (idBarang != null) {
                idBarang.getHistoryLelangCollection().remove(historyLelang);
                idBarang = em.merge(idBarang);
            }
            TbMasyarakat idUser = historyLelang.getIdUser();
            if (idUser != null) {
                idUser.getHistoryLelangCollection().remove(historyLelang);
                idUser = em.merge(idUser);
            }
            TbLelang idLelang = historyLelang.getIdLelang();
            if (idLelang != null) {
                idLelang.getHistoryLelangCollection().remove(historyLelang);
                idLelang = em.merge(idLelang);
            }
            em.remove(historyLelang);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistoryLelang> findHistoryLelangEntities() {
        return findHistoryLelangEntities(true, -1, -1);
    }

    public List<HistoryLelang> findHistoryLelangEntities(int maxResults, int firstResult) {
        return findHistoryLelangEntities(false, maxResults, firstResult);
    }

    private List<HistoryLelang> findHistoryLelangEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistoryLelang.class));
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

    public HistoryLelang findHistoryLelang(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistoryLelang.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoryLelangCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistoryLelang> rt = cq.from(HistoryLelang.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
