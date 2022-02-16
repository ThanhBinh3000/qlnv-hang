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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "QLNV_TTIN_DTHAU_HHOA_DTL")
@Data
public class QlnvTtinDthauHhoaDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_TTIN_DTHAU_HHOA_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_TTIN_DTHAU_HHOA_DTL_SEQ", allocationSize = 1, name = "QLNV_TTIN_DTHAU_HHOA_DTL_SEQ")
	private Long id;
	String tenGoiThau;
	String soHieu;
	BigDecimal soLuong;
	BigDecimal giaThau;
	String hthucLcnt;
	String pthucLcnt;
	Date tuNgayLcnt;
	Date denNgayLcnt;
	String loaiHdong;
	BigDecimal tgianHdong;
	String maDvi;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
    private QlnvTtinDthauHhoaHdr header;
	
	@OneToMany(
	        mappedBy = "dtl1",
	        		fetch = FetchType.LAZY, cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	
	private List<QlnvTtinDthauHhoaDtl1> detail1List = new ArrayList<QlnvTtinDthauHhoaDtl1>();
	
	public void setDetail1List(List<QlnvTtinDthauHhoaDtl1> detail) {
        if (this.detail1List == null) {
            this.detail1List = detail;
        } else if(this.detail1List != detail) {
            this.detail1List.clear();
            if(detail != null){
                this.detail1List.addAll(detail);
            }
        }
    }
	
	public QlnvTtinDthauHhoaDtl addDetail1(QlnvTtinDthauHhoaDtl1 dt) {
		detail1List.add(dt);
        dt.setDtl1(this);
        return this;
    }
 
    public QlnvTtinDthauHhoaDtl removeDetail1(QlnvTtinDthauHhoaDtl1 dt) {
    	detail1List.remove(dt);
        dt.setDtl1(null);
        return this;
    }
}
