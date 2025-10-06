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
@Table(name = "tb_barang")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TbBarang.findAll", query = "SELECT t FROM TbBarang t")
    , @NamedQuery(name = "TbBarang.findByIdBarang", query = "SELECT t FROM TbBarang t WHERE t.idBarang = :idBarang")
    , @NamedQuery(name = "TbBarang.findByNamaBarang", query = "SELECT t FROM TbBarang t WHERE t.namaBarang = :namaBarang")
    , @NamedQuery(name = "TbBarang.findByTgl", query = "SELECT t FROM TbBarang t WHERE t.tgl = :tgl")
    , @NamedQuery(name = "TbBarang.findByHargaAwal", query = "SELECT t FROM TbBarang t WHERE t.hargaAwal = :hargaAwal")
    , @NamedQuery(name = "TbBarang.findByDeskripsiBarang", query = "SELECT t FROM TbBarang t WHERE t.deskripsiBarang = :deskripsiBarang")})
public class TbBarang implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_barang")
    private Integer idBarang;
    @Basic(optional = false)
    @Column(name = "nama_barang")
    private String namaBarang;
    @Basic(optional = false)
    @Column(name = "tgl")
    @Temporal(TemporalType.DATE)
    private Date tgl;
    @Basic(optional = false)
    @Column(name = "harga_awal")
    private int hargaAwal;
    @Basic(optional = false)
    @Column(name = "deskripsi_barang")
    private String deskripsiBarang;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBarang")
    private Collection<HistoryLelang> historyLelangCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBarang")
    private Collection<TbLelang> tbLelangCollection;

    public TbBarang() {
    }

    public TbBarang(Integer idBarang) {
        this.idBarang = idBarang;
    }

    public TbBarang(Integer idBarang, String namaBarang, Date tgl, int hargaAwal, String deskripsiBarang) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.tgl = tgl;
        this.hargaAwal = hargaAwal;
        this.deskripsiBarang = deskripsiBarang;
    }

    public Integer getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(Integer idBarang) {
        this.idBarang = idBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public Date getTgl() {
        return tgl;
    }

    public void setTgl(Date tgl) {
        this.tgl = tgl;
    }

    public int getHargaAwal() {
        return hargaAwal;
    }

    public void setHargaAwal(int hargaAwal) {
        this.hargaAwal = hargaAwal;
    }

    public String getDeskripsiBarang() {
        return deskripsiBarang;
    }

    public void setDeskripsiBarang(String deskripsiBarang) {
        this.deskripsiBarang = deskripsiBarang;
    }

    @XmlTransient
    public Collection<HistoryLelang> getHistoryLelangCollection() {
        return historyLelangCollection;
    }

    public void setHistoryLelangCollection(Collection<HistoryLelang> historyLelangCollection) {
        this.historyLelangCollection = historyLelangCollection;
    }

    @XmlTransient
    public Collection<TbLelang> getTbLelangCollection() {
        return tbLelangCollection;
    }

    public void setTbLelangCollection(Collection<TbLelang> tbLelangCollection) {
        this.tbLelangCollection = tbLelangCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBarang != null ? idBarang.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbBarang)) {
            return false;
        }
        TbBarang other = (TbBarang) object;
        if ((this.idBarang == null && other.idBarang != null) || (this.idBarang != null && !this.idBarang.equals(other.idBarang))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lelangonline.TbBarang[ idBarang=" + idBarang + " ]";
    }
    
}
