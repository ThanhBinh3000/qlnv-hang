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
@Table(name = "QLNV_TTIN_DTHAU_VTU_DTL")
@Data
public class QlnvTtinDthauVtuDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_TTIN_DTHAU_VTU_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_TTIN_DTHAU_VTU_DTL_SEQ", allocationSize = 1, name = "QLNV_TTIN_DTHAU_VTU_DTL_SEQ")
	private Long id;
	String tenGoiThau;
	String soHieu;
	BigDecimal soLuong;
	BigDecimal giaThau;
	String hthucLcnt;
	String pthucLcnt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date tuNgayLcnt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date denNgayLcnt;
	String loaiHdong;
	BigDecimal tgianHdong;
	String tenPage;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date ngayDangPage;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date tuNgayPhanhHsmt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date denNgayPhanhHsmt;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	Date ngayMothau;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	Date ngayDongThau;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
    private QlnvTtinDthauVtuHdr header;
	
	@OneToMany(
	        mappedBy = "dtl1",
	        		fetch = FetchType.LAZY, cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	
	private List<QlnvTtinDthauVtuDtl1> detail1List = new ArrayList<QlnvTtinDthauVtuDtl1>();
	@OneToMany(
	        mappedBy = "dtl2",
	        		fetch = FetchType.LAZY, cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	private List<QlnvTtinDthauVtuDtl2> detail2List = new ArrayList<QlnvTtinDthauVtuDtl2>();
	
	public void setDetail1List(List<QlnvTtinDthauVtuDtl1> detail) {
        if (this.detail1List == null) {
            this.detail1List = detail;
        } else if(this.detail1List != detail) {
            this.detail1List.clear();
            if(detail != null){
                this.detail1List.addAll(detail);
            }
        }
    }
	public void setDetail2List(List<QlnvTtinDthauVtuDtl2> detail) {
        if (this.detail2List == null) {
            this.detail2List = detail;
        } else if(this.detail2List != detail) {
            this.detail2List.clear();
            if(detail != null){
                this.detail2List.addAll(detail);
            }
        }
    }
	
	public QlnvTtinDthauVtuDtl addDetail1(QlnvTtinDthauVtuDtl1 dt) {
		detail1List.add(dt);
        dt.setDtl1(this);
        return this;
    }
 
    public QlnvTtinDthauVtuDtl removeDetail1(QlnvTtinDthauVtuDtl1 dt) {
    	detail1List.remove(dt);
        dt.setDtl1(null);
        return this;
    }
	
	public QlnvTtinDthauVtuDtl addDetail2(QlnvTtinDthauVtuDtl2 dt) {
		detail2List.add(dt);
        dt.setDtl2(this);
        return this;
    }
 
    public QlnvTtinDthauVtuDtl removeDetail2(QlnvTtinDthauVtuDtl2 dt) {
    	detail2List.remove(dt);
        dt.setDtl2(null);
        return this;
    }
}
