/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lelangonline;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author fikri
 */
@Entity
@Table(name = "history_lelang")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistoryLelang.findAll", query = "SELECT h FROM HistoryLelang h")
    , @NamedQuery(name = "HistoryLelang.findByIdHistory", query = "SELECT h FROM HistoryLelang h WHERE h.idHistory = :idHistory")
    , @NamedQuery(name = "HistoryLelang.findByPenawaranHarga", query = "SELECT h FROM HistoryLelang h WHERE h.penawaranHarga = :penawaranHarga")})
public class HistoryLelang implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_history")
    private Integer idHistory;
    @Basic(optional = false)
    @Column(name = "penawaran_harga")
    private int penawaranHarga;
    @JoinColumn(name = "id_barang", referencedColumnName = "id_barang")
    @ManyToOne(optional = false)
    private TbBarang idBarang;
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ManyToOne(optional = false)
    private TbMasyarakat idUser;
    @JoinColumn(name = "id_lelang", referencedColumnName = "id_lelang")
    @ManyToOne(optional = false)
    private TbLelang idLelang;

    public HistoryLelang() {
    }

    public HistoryLelang(Integer idHistory) {
        this.idHistory = idHistory;
    }

    public HistoryLelang(Integer idHistory, int penawaranHarga) {
        this.idHistory = idHistory;
        this.penawaranHarga = penawaranHarga;
    }

    public Integer getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(Integer idHistory) {
        this.idHistory = idHistory;
    }

    public int getPenawaranHarga() {
        return penawaranHarga;
    }

    public void setPenawaranHarga(int penawaranHarga) {
        this.penawaranHarga = penawaranHarga;
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

    public TbLelang getIdLelang() {
        return idLelang;
    }

    public void setIdLelang(TbLelang idLelang) {
        this.idLelang = idLelang;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistory != null ? idHistory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoryLelang)) {
            return false;
        }
        HistoryLelang other = (HistoryLelang) object;
        if ((this.idHistory == null && other.idHistory != null) || (this.idHistory != null && !this.idHistory.equals(other.idHistory))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lelangonline.HistoryLelang[ idHistory=" + idHistory + " ]";
    }
    
}
