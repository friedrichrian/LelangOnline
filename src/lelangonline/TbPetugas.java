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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "tb_petugas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TbPetugas.findAll", query = "SELECT t FROM TbPetugas t")
    , @NamedQuery(name = "TbPetugas.findByIdPetugas", query = "SELECT t FROM TbPetugas t WHERE t.idPetugas = :idPetugas")
    , @NamedQuery(name = "TbPetugas.findByNamaPetugas", query = "SELECT t FROM TbPetugas t WHERE t.namaPetugas = :namaPetugas")
    , @NamedQuery(name = "TbPetugas.findByUsername", query = "SELECT t FROM TbPetugas t WHERE t.username = :username")
    , @NamedQuery(name = "TbPetugas.findByPassword", query = "SELECT t FROM TbPetugas t WHERE t.password = :password")})
public class TbPetugas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_petugas")
    private Integer idPetugas;
    @Basic(optional = false)
    @Column(name = "nama_petugas")
    private String namaPetugas;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @JoinColumn(name = "id_level", referencedColumnName = "id_level")
    @ManyToOne(optional = false)
    private TbLevel idLevel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPetugas")
    private Collection<TbLelang> tbLelangCollection;

    public TbPetugas() {
    }

    public TbPetugas(Integer idPetugas) {
        this.idPetugas = idPetugas;
    }

    public TbPetugas(Integer idPetugas, String namaPetugas, String username, String password) {
        this.idPetugas = idPetugas;
        this.namaPetugas = namaPetugas;
        this.username = username;
        this.password = password;
    }

    public Integer getIdPetugas() {
        return idPetugas;
    }

    public void setIdPetugas(Integer idPetugas) {
        this.idPetugas = idPetugas;
    }

    public String getNamaPetugas() {
        return namaPetugas;
    }

    public void setNamaPetugas(String namaPetugas) {
        this.namaPetugas = namaPetugas;
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

    public TbLevel getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(TbLevel idLevel) {
        this.idLevel = idLevel;
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
        hash += (idPetugas != null ? idPetugas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbPetugas)) {
            return false;
        }
        TbPetugas other = (TbPetugas) object;
        if ((this.idPetugas == null && other.idPetugas != null) || (this.idPetugas != null && !this.idPetugas.equals(other.idPetugas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lelangonline.TbPetugas[ idPetugas=" + idPetugas + " ]";
    }
    
}
