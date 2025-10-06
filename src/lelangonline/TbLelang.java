/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lelangonline;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author fikri
 */
@Entity
@Table(name = "tb_lelang")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TbLelang.findAll", query = "SELECT t FROM TbLelang t")
    , @NamedQuery(name = "TbLelang.findByIdLelang", query = "SELECT t FROM TbLelang t WHERE t.idLelang = :idLelang")
    , @NamedQuery(name = "TbLelang.findByTglLelang", query = "SELECT t FROM TbLelang t WHERE t.tglLelang = :tglLelang")
    , @NamedQuery(name = "TbLelang.findByHargaAkhir", query = "SELECT t FROM TbLelang t WHERE t.hargaAkhir = :hargaAkhir")
    , @NamedQuery(name = "TbLelang.findByStatus", query = "SELECT t FROM TbLelang t WHERE t.status = :status")})
public class TbLelang implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_lelang")
    private Integer idLelang;
    @Basic(optional = false)
    @Column(name = "tgl_lelang")
    @Temporal(TemporalType.DATE)
    private Date tglLelang;
    @Basic(optional = false)
    @Column(name = "harga_akhir")
    private int hargaAkhir;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLelang")
    private Collection<HistoryLelang> historyLelangCollection;
    @JoinColumn(name = "id_barang", referencedColumnName = "id_barang")
    @ManyToOne(optional = false)
    private TbBarang idBarang;
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ManyToOne(optional = false)
    private TbMasyarakat idUser;
    @JoinColumn(name = "id_petugas", referencedColumnName = "id_petugas")
    @ManyToOne(optional = false)
    private TbPetugas idPetugas;

    public TbLelang() {
    }

    public TbLelang(Integer idLelang) {
        this.idLelang = idLelang;
    }

    public TbLelang(Integer idLelang, Date tglLelang, int hargaAkhir, String status) {
        this.idLelang = idLelang;
        this.tglLelang = tglLelang;
        this.hargaAkhir = hargaAkhir;
        this.status = status;
    }

    public Integer getIdLelang() {
        return idLelang;
    }

    public void setIdLelang(Integer idLelang) {
        this.idLelang = idLelang;
    }

    public Date getTglLelang() {
        return tglLelang;
    }

    public void setTglLelang(Date tglLelang) {
        this.tglLelang = tglLelang;
    }

    public int getHargaAkhir() {
        return hargaAkhir;
    }

    public void setHargaAkhir(int hargaAkhir) {
        this.hargaAkhir = hargaAkhir;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<HistoryLelang> getHistoryLelangCollection() {
        return historyLelangCollection;
    }

    public void setHistoryLelangCollection(Collection<HistoryLelang> historyLelangCollection) {
        this.historyLelangCollection = historyLelangCollection;
    }

    public TbBarang getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(TbBarang idBarang) {
        this.idBarang = idBarang;
    }

    public TbMasyarakat getIdUser() {
        return idUser;
    }

    public void setIdUser(TbMasyarakat idUser) {
        this.idUser = idUser;
    }

    public TbPetugas getIdPetugas() {
        return idPetugas;
    }

    public void setIdPetugas(TbPetugas idPetugas) {
        this.idPetugas = idPetugas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLelang != null ? idLelang.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbLelang)) {
            return false;
        }
        TbLelang other = (TbLelang) object;
        if ((this.idLelang == null && other.idLelang != null) || (this.idLelang != null && !this.idLelang.equals(other.idLelang))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lelangonline.TbLelang[ idLelang=" + idLelang + " ]";
    }
    
}
