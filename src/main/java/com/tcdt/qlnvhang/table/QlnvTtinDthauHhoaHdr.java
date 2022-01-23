package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "QLNV_TTIN_DTHAU_HHOA_HDR")
@Data
public class QlnvTtinDthauHhoaHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_TTIN_DTHAU_HHOA_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_TTIN_DTHAU_HHOA_HDR_SEQ", allocationSize = 1, name = "QLNV_TTIN_DTHAU_HHOA_HDR_SEQ")
	private Long id;
	String maDvi;
	String soQdKh;
	Date ngayQdKh;
	String maHhoa;
	String tenHhoa;
	String dviTinh;
	String nguonVon;
	String soQdLquan;
	String moTa;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	String tenPage;
	Date ngayDangPage;
	Date tuNgayPhanhHsmt;
	Date denNgayPhanhHsmt;
	Date ngayMothau;
	Date ngayDongthau;
	
	@OneToMany(
	        mappedBy = "header",
	        		fetch = FetchType.LAZY, cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	
	private List<QlnvTtinDthauHhoaDtl> detailList = new ArrayList<QlnvTtinDthauHhoaDtl>();
	
	public void setDetailList(List<QlnvTtinDthauHhoaDtl> detail) {
        if (this.detailList == null) {
            this.detailList = detail;
        } else if(this.detailList != detail) {
            this.detailList.clear();
            if(detail != null){
                this.detailList.addAll(detail);
            }
        }
    }
	
	public QlnvTtinDthauHhoaHdr addDetail(QlnvTtinDthauHhoaDtl dt) {
		detailList.add(dt);
        dt.setHeader(this); 
        return this;
    }
 
    public QlnvTtinDthauHhoaHdr removeDetail(QlnvTtinDthauHhoaDtl dt) {
    	detailList.remove(dt);
        dt.setHeader(null);
        return this;
    }
}
