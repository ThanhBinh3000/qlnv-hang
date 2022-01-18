package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "QLNV_KH_LCNT_VTU_DTL")
@Data
public class QlnvKhLcntVtuDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_KH_LCNT_VTU_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_KH_LCNT_VTU_DTL_SEQ", allocationSize = 1, name = "QLNV_KH_LCNT_VTU_DTL_SEQ")
	private Long id;
	String tenGoiThau;
	Integer soLuong;
	BigDecimal donGia;
	@Lob
	String doc;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kh_lcnt_vtu_id")
    private QlnvKhLcntVtuHdr header;
	
	@OneToMany(
	        mappedBy = "parent",
	        		fetch = FetchType.LAZY, cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	private List<QlnvKhLcntVtuDtlCtiet> children = new ArrayList<QlnvKhLcntVtuDtlCtiet>();
	
	public void setChildren(List<QlnvKhLcntVtuDtlCtiet> children) {
        if (this.children == null) {
            this.children = children;
        } else if(this.children != children) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
    		
            this.children.clear();
            for (QlnvKhLcntVtuDtlCtiet child : children) {
    			child.setParent(this);
    		}
    		this.children.addAll(children);
        }
    }
	
	public QlnvKhLcntVtuDtl addDetail(QlnvKhLcntVtuDtlCtiet dt) {
		children.add(dt);
        dt.setParent(this);
        return this;
    }
 
    public QlnvKhLcntVtuDtl removeDetail(QlnvKhLcntVtuDtlCtiet dt) {
    	children.remove(dt);
        dt.setParent(null);
        return this;
    }
}
