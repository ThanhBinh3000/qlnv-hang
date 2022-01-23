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

import lombok.Data;

@Entity
@Table(name = "QLNV_TTIN_DTHAU_VTU_HDR")
@Data
public class QlnvTtinDthauVtuHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_TTIN_DTHAU_VTU_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_TTIN_DTHAU_VTU_HDR_SEQ", allocationSize = 1, name = "QLNV_TTIN_DTHAU_VTU_HDR_SEQ")
	private Long id;
	String maDvi;
	String soQdKh;
	Date ngayQdKh;
	String maVtu;
	String tenVtu;
	String dviTinh;
	String nguonVon;
	String soQdLquan;
	String moTa;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	@OneToMany(
	        mappedBy = "header",
	        		fetch = FetchType.LAZY, cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	
	private List<QlnvTtinDthauVtuDtl> detailList = new ArrayList<QlnvTtinDthauVtuDtl>();
	
	public void setDetailList(List<QlnvTtinDthauVtuDtl> detail) {
        if (this.detailList == null) {
            this.detailList = detail;
        } else if(this.detailList != detail) {
            this.detailList.clear();
            if(detail != null){
                this.detailList.addAll(detail);
            }
        }
    }
	
	public QlnvTtinDthauVtuHdr addDetail(QlnvTtinDthauVtuDtl dt) {
		detailList.add(dt);
        dt.setHeader(this);
        return this;
    }
 
    public QlnvTtinDthauVtuHdr removeDetail(QlnvTtinDthauVtuDtl dt) {
    	detailList.remove(dt);
        dt.setHeader(null);
        return this;
    }
}
