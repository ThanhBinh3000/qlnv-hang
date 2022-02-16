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
@Table(name = "QLNV_QD_LCNT_VTU_HDR")
@Data
public class QlnvQdLcntVtuHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_LCNT_VTU_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_LCNT_VTU_HDR_SEQ", allocationSize = 1, name = "QLNV_QD_LCNT_VTU_HDR_SEQ")
	private Long id;
	String soQdinh;
	Date ngayQd;
	String soQdKh;
	String loaiHanghoa;
	String maHanghoa;
	String tcKiThuat;
	String loaiQd;
	String soQdinhGoc;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	String ldoTuchoi;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	Date ngayPduyet;
	String nguoiPduyet;
	Date ngayDx;
	Date ngayQdGoc;
	String soDxuat;
	String loaiDieuChinh;
	//@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@OneToMany(
	        mappedBy = "header",
	        		fetch = FetchType.LAZY, cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	private List<QlnvQdLcntVtuDtl> detailList = new ArrayList<QlnvQdLcntVtuDtl>();
	
	public void setDetailList(List<QlnvQdLcntVtuDtl> details) {
        if (this.detailList == null) {
            this.detailList = details;
        } else if(this.detailList != details) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
        	 this.detailList.clear();
             for (QlnvQdLcntVtuDtl detail : details) {
             	detail.setHeader(this);
     		}
     		this.detailList.addAll(details);
        }
    }
	
	public QlnvQdLcntVtuHdr addDetail(QlnvQdLcntVtuDtl dt) {
		detailList.add(dt);
        dt.setHeader(this);
        return this;
    }
 
    public QlnvQdLcntVtuHdr removeDetail(QlnvQdLcntVtuDtl dt) {
    	detailList.remove(dt);
        dt.setHeader(null);
        return this;
    }
}
