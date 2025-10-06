/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lelangonline;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author fikri
 */
@Entity
@Table(name = "tb_masyarakat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TbMasyarakat.findAll", query = "SELECT t FROM TbMasyarakat t")
    , @NamedQuery(name = "TbMasyarakat.findByIdUser", query = "SELECT t FROM TbMasyarakat t WHERE t.idUser = :idUser")
    , @NamedQuery(name = "TbMasyarakat.findByNamaLengkap", query = "SELECT t FROM TbMasyarakat t WHERE t.namaLengkap = :namaLengkap")
    , @NamedQuery(name = "TbMasyarakat.findByUsername", query = "SELECT t FROM TbMasyarakat t WHERE t.username = :username")
    , @NamedQuery(name = "TbMasyarakat.findByPassword", query = "SELECT t FROM TbMasyarakat t WHERE t.password = :password")
    , @NamedQuery(name = "TbMasyarakat.findByTelp", query = "SELECT t FROM TbMasyarakat t WHERE t.telp = :telp")})
public class TbMasyarakat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_user")
    private Integer idUser;
    @Basic(optional = false)
    @Column(name = "nama_lengkap")
    private String namaLengkap;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "telp")
    private String telp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser")
    private Collection<HistoryLelang> historyLelangCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser")
    private Collection<TbLelang> tbLelangCollection;

    public TbMasyarakat() {
    }

    public TbMasyarakat(Integer idUser) {
        this.idUser = idUser;
    }

    public TbMasyarakat(Integer idUser, String namaLengkap, String username, String password, String telp) {
        this.idUser = idUser;
        this.namaLengkap = namaLengkap;
        this.username = username;
        this.password = password;
        this.telp = telp;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
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
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbMasyarakat)) {
            return false;
        }
        TbMasyarakat other = (TbMasyarakat) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lelangonline.TbMasyarakat[ idUser=" + idUser + " ]";
    }
    
}
