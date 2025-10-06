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
@Table(name = "tb_level")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TbLevel.findAll", query = "SELECT t FROM TbLevel t")
    , @NamedQuery(name = "TbLevel.findByIdLevel", query = "SELECT t FROM TbLevel t WHERE t.idLevel = :idLevel")
    , @NamedQuery(name = "TbLevel.findByLevel", query = "SELECT t FROM TbLevel t WHERE t.level = :level")})
public class TbLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_level")
    private Integer idLevel;
    @Basic(optional = false)
    @Column(name = "level")
    private String level;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLevel")
    private Collection<TbPetugas> tbPetugasCollection;

    public TbLevel() {
    }

    public TbLevel(Integer idLevel) {
        this.idLevel = idLevel;
    }

    public TbLevel(Integer idLevel, String level) {
        this.idLevel = idLevel;
        this.level = level;
    }

    public Integer getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(Integer idLevel) {
        this.idLevel = idLevel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @XmlTransient
    public Collection<TbPetugas> getTbPetugasCollection() {
        return tbPetugasCollection;
    }

    public void setTbPetugasCollection(Collection<TbPetugas> tbPetugasCollection) {
        this.tbPetugasCollection = tbPetugasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLevel != null ? idLevel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbLevel)) {
            return false;
        }
        TbLevel other = (TbLevel) object;
        if ((this.idLevel == null && other.idLevel != null) || (this.idLevel != null && !this.idLevel.equals(other.idLevel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lelangonline.TbLevel[ idLevel=" + idLevel + " ]";
    }
    
}
