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
public class TbMasyarakatJpaController implements Serializable {

    public TbMasyarakatJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TbMasyarakat tbMasyarakat) throws PreexistingEntityException, Exception {
        if (tbMasyarakat.getHistoryLelangCollection() == null) {
            tbMasyarakat.setHistoryLelangCollection(new ArrayList<HistoryLelang>());
        }
        if (tbMasyarakat.getTbLelangCollection() == null) {
            tbMasyarakat.setTbLelangCollection(new ArrayList<TbLelang>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<HistoryLelang> attachedHistoryLelangCollection = new ArrayList<HistoryLelang>();
            for (HistoryLelang historyLelangCollectionHistoryLelangToAttach : tbMasyarakat.getHistoryLelangCollection()) {
                historyLelangCollectionHistoryLelangToAttach = em.getReference(historyLelangCollectionHistoryLelangToAttach.getClass(), historyLelangCollectionHistoryLelangToAttach.getIdHistory());
                attachedHistoryLelangCollection.add(historyLelangCollectionHistoryLelangToAttach);
            }
            tbMasyarakat.setHistoryLelangCollection(attachedHistoryLelangCollection);
            Collection<TbLelang> attachedTbLelangCollection = new ArrayList<TbLelang>();
            for (TbLelang tbLelangCollectionTbLelangToAttach : tbMasyarakat.getTbLelangCollection()) {
                tbLelangCollectionTbLelangToAttach = em.getReference(tbLelangCollectionTbLelangToAttach.getClass(), tbLelangCollectionTbLelangToAttach.getIdLelang());
                attachedTbLelangCollection.add(tbLelangCollectionTbLelangToAttach);
            }
            tbMasyarakat.setTbLelangCollection(attachedTbLelangCollection);
            em.persist(tbMasyarakat);
            for (HistoryLelang historyLelangCollectionHistoryLelang : tbMasyarakat.getHistoryLelangCollection()) {
                TbMasyarakat oldIdUserOfHistoryLelangCollectionHistoryLelang = historyLelangCollectionHistoryLelang.getIdUser();
                historyLelangCollectionHistoryLelang.setIdUser(tbMasyarakat);
                historyLelangCollectionHistoryLelang = em.merge(historyLelangCollectionHistoryLelang);
                if (oldIdUserOfHistoryLelangCollectionHistoryLelang != null) {
                    oldIdUserOfHistoryLelangCollectionHistoryLelang.getHistoryLelangCollection().remove(historyLelangCollectionHistoryLelang);
                    oldIdUserOfHistoryLelangCollectionHistoryLelang = em.merge(oldIdUserOfHistoryLelangCollectionHistoryLelang);
                }
            }
            for (TbLelang tbLelangCollectionTbLelang : tbMasyarakat.getTbLelangCollection()) {
                TbMasyarakat oldIdUserOfTbLelangCollectionTbLelang = tbLelangCollectionTbLelang.getIdUser();
                tbLelangCollectionTbLelang.setIdUser(tbMasyarakat);
                tbLelangCollectionTbLelang = em.merge(tbLelangCollectionTbLelang);
                if (oldIdUserOfTbLelangCollectionTbLelang != null) {
                    oldIdUserOfTbLelangCollectionTbLelang.getTbLelangCollection().remove(tbLelangCollectionTbLelang);
                    oldIdUserOfTbLelangCollectionTbLelang = em.merge(oldIdUserOfTbLelangCollectionTbLelang);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTbMasyarakat(tbMasyarakat.getIdUser()) != null) {
                throw new PreexistingEntityException("TbMasyarakat " + tbMasyarakat + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbMasyarakat tbMasyarakat) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbMasyarakat persistentTbMasyarakat = em.find(TbMasyarakat.class, tbMasyarakat.getIdUser());
            Collection<HistoryLelang> historyLelangCollectionOld = persistentTbMasyarakat.getHistoryLelangCollection();
            Collection<HistoryLelang> historyLelangCollectionNew = tbMasyarakat.getHistoryLelangCollection();
            Collection<TbLelang> tbLelangCollectionOld = persistentTbMasyarakat.getTbLelangCollection();
            Collection<TbLelang> tbLelangCollectionNew = tbMasyarakat.getTbLelangCollection();
            List<String> illegalOrphanMessages = null;
            for (HistoryLelang historyLelangCollectionOldHistoryLelang : historyLelangCollectionOld) {
                if (!historyLelangCollectionNew.contains(historyLelangCollectionOldHistoryLelang)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistoryLelang " + historyLelangCollectionOldHistoryLelang + " since its idUser field is not nullable.");
                }
            }
            for (TbLelang tbLelangCollectionOldTbLelang : tbLelangCollectionOld) {
                if (!tbLelangCollectionNew.contains(tbLelangCollectionOldTbLelang)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TbLelang " + tbLelangCollectionOldTbLelang + " since its idUser field is not nullable.");
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
            tbMasyarakat.setHistoryLelangCollection(historyLelangCollectionNew);
            Collection<TbLelang> attachedTbLelangCollectionNew = new ArrayList<TbLelang>();
            for (TbLelang tbLelangCollectionNewTbLelangToAttach : tbLelangCollectionNew) {
                tbLelangCollectionNewTbLelangToAttach = em.getReference(tbLelangCollectionNewTbLelangToAttach.getClass(), tbLelangCollectionNewTbLelangToAttach.getIdLelang());
                attachedTbLelangCollectionNew.add(tbLelangCollectionNewTbLelangToAttach);
            }
            tbLelangCollectionNew = attachedTbLelangCollectionNew;
            tbMasyarakat.setTbLelangCollection(tbLelangCollectionNew);
            tbMasyarakat = em.merge(tbMasyarakat);
            for (HistoryLelang historyLelangCollectionNewHistoryLelang : historyLelangCollectionNew) {
                if (!historyLelangCollectionOld.contains(historyLelangCollectionNewHistoryLelang)) {
                    TbMasyarakat oldIdUserOfHistoryLelangCollectionNewHistoryLelang = historyLelangCollectionNewHistoryLelang.getIdUser();
                    historyLelangCollectionNewHistoryLelang.setIdUser(tbMasyarakat);
                    historyLelangCollectionNewHistoryLelang = em.merge(historyLelangCollectionNewHistoryLelang);
                    if (oldIdUserOfHistoryLelangCollectionNewHistoryLelang != null && !oldIdUserOfHistoryLelangCollectionNewHistoryLelang.equals(tbMasyarakat)) {
                        oldIdUserOfHistoryLelangCollectionNewHistoryLelang.getHistoryLelangCollection().remove(historyLelangCollectionNewHistoryLelang);
                        oldIdUserOfHistoryLelangCollectionNewHistoryLelang = em.merge(oldIdUserOfHistoryLelangCollectionNewHistoryLelang);
                    }
                }
            }
            for (TbLelang tbLelangCollectionNewTbLelang : tbLelangCollectionNew) {
                if (!tbLelangCollectionOld.contains(tbLelangCollectionNewTbLelang)) {
                    TbMasyarakat oldIdUserOfTbLelangCollectionNewTbLelang = tbLelangCollectionNewTbLelang.getIdUser();
                    tbLelangCollectionNewTbLelang.setIdUser(tbMasyarakat);
                    tbLelangCollectionNewTbLelang = em.merge(tbLelangCollectionNewTbLelang);
                    if (oldIdUserOfTbLelangCollectionNewTbLelang != null && !oldIdUserOfTbLelangCollectionNewTbLelang.equals(tbMasyarakat)) {
                        oldIdUserOfTbLelangCollectionNewTbLelang.getTbLelangCollection().remove(tbLelangCollectionNewTbLelang);
                        oldIdUserOfTbLelangCollectionNewTbLelang = em.merge(oldIdUserOfTbLelangCollectionNewTbLelang);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tbMasyarakat.getIdUser();
                if (findTbMasyarakat(id) == null) {
                    throw new NonexistentEntityException("The tbMasyarakat with id " + id + " no longer exists.");
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
            TbMasyarakat tbMasyarakat;
            try {
                tbMasyarakat = em.getReference(TbMasyarakat.class, id);
                tbMasyarakat.getIdUser();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tbMasyarakat with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<HistoryLelang> historyLelangCollectionOrphanCheck = tbMasyarakat.getHistoryLelangCollection();
            for (HistoryLelang historyLelangCollectionOrphanCheckHistoryLelang : historyLelangCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TbMasyarakat (" + tbMasyarakat + ") cannot be destroyed since the HistoryLelang " + historyLelangCollectionOrphanCheckHistoryLelang + " in its historyLelangCollection field has a non-nullable idUser field.");
            }
            Collection<TbLelang> tbLelangCollectionOrphanCheck = tbMasyarakat.getTbLelangCollection();
            for (TbLelang tbLelangCollectionOrphanCheckTbLelang : tbLelangCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TbMasyarakat (" + tbMasyarakat + ") cannot be destroyed since the TbLelang " + tbLelangCollectionOrphanCheckTbLelang + " in its tbLelangCollection field has a non-nullable idUser field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tbMasyarakat);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbMasyarakat> findTbMasyarakatEntities() {
        return findTbMasyarakatEntities(true, -1, -1);
    }

    public List<TbMasyarakat> findTbMasyarakatEntities(int maxResults, int firstResult) {
        return findTbMasyarakatEntities(false, maxResults, firstResult);
    }

    private List<TbMasyarakat> findTbMasyarakatEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbMasyarakat.class));
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

    public TbMasyarakat findTbMasyarakat(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbMasyarakat.class, id);
        } finally {
            em.close();
        }
    }

    public int getTbMasyarakatCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbMasyarakat> rt = cq.from(TbMasyarakat.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public static void main(String[] args) {
        // 1️⃣ Buat EntityManagerFactory dari persistence.xml
        javax.persistence.EntityManagerFactory emf = 
            javax.persistence.Persistence.createEntityManagerFactory("LelangOnlinePU"); 
        // Ganti "lelangonlinePU" sesuai dengan nama persistence-unit di persistence.xml kamu

        // 2️⃣ Buat instance controller
        TbMasyarakatJpaController controller = new TbMasyarakatJpaController(emf);

        // 3️⃣ Ambil semua data masyarakat
        List<TbMasyarakat> list = controller.findTbMasyarakatEntities();

        // 4️⃣ Cek hasilnya
        if (list.isEmpty()) {
            System.out.println("Tidak ada data masyarakat di database.");
        } else {
            System.out.println("===== DAFTAR MASYARAKAT =====");
            for (TbMasyarakat m : list) {
                System.out.println("ID: " + m.getIdUser() + 
                                   ", Username: " + m.getUsername() + 
                                   ", Nama: " + m.getNamaLengkap());
            }
        }

        // 5️⃣ Tutup EMF
        emf.close();
    }

}
