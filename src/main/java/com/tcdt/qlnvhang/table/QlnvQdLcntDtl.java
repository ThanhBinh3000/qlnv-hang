package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_LCNT_DTL")
@Data
public class QlnvQdLcntDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_LCNT_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_LCNT_DTL_SEQ", allocationSize = 1, name = "QLNV_QD_LCNT_DTL_SEQ")
	private Long id;
	String soDxuat;
	String maDvi;
	BigDecimal soGoithau;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
    private QlnvQdLcntHdr header;
	
	//@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
		@OneToMany(
		        mappedBy = "dtl",
		        		fetch = FetchType.LAZY, cascade = CascadeType.ALL,
		        orphanRemoval = true
		    )
		private List<QlnvQdLcntDtlCtiet> detailList = new ArrayList<QlnvQdLcntDtlCtiet>();
		
		public void setDetailList(List<QlnvQdLcntDtlCtiet> detail) {
	        if (this.detailList == null) {
	            this.detailList = detail;
	        } else if(this.detailList != detail) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
	            this.detailList.clear();
	            if(detail != null){
	                this.detailList.addAll(detail);
	            }
	        }
	    }
		
		public QlnvQdLcntDtl addDetail(QlnvQdLcntDtlCtiet dt) {
			detailList.add(dt);
	        dt.setDtl(this);
	        return this;
	    }
	 
	    public QlnvQdLcntDtl removeDetail(QlnvQdLcntDtlCtiet dt) {
	    	detailList.remove(dt);
	        dt.setDtl(null);
	        return this;
	    }
}
