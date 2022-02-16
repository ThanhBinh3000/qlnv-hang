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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QLNV_BBAN_HAODOI_HDR")
@Data
public class QlnvBbanHaodoiHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_BBAN_HAODOI_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_BBAN_HAODOI_HDR_SEQ", allocationSize = 1, name = "QLNV_BBAN_HAODOI_HDR_SEQ")
	private Long id;
	
	String soBban;
	String maDvi;
	String maKho;
	Date ngayLap;
	String maHhoa;
	String tenHhoa;
	String maLo;
	String maThukho;
	String diaDiem;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	String ldoTuchoi;
	Date ngayPduyet;
	String nguoiPduyet;
	String maNgan;
	String kthuatVien;
	String keToan;
	String thuTruong;
	BigDecimal sluongNhap;
	BigDecimal sluongXuat;
	String dviTinh;
	Date ngayKthucNhap;
	String nguyenNhan;
	String kienNghi;
	String soBbanTinhkho;
	Date ngayKthucXuat;
	BigDecimal sluongHaoTte;
	Double tleHaoTte;
	BigDecimal sluongHaoTly;
	Double tleHaoTly;
	BigDecimal sluongHaoTrendm;
	Double tleHaoTrendm;
	BigDecimal sluongHaoDuoidm;
	Double tleHaoDuoidm;
	String thuKho;
	String trangThai;
	
	@OneToMany(
	        mappedBy = "header",
	        		fetch = FetchType.LAZY, cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	
	private List<QlnvBbanHaodoiDtl> detailList = new ArrayList<QlnvBbanHaodoiDtl>();
	
	public void setDetailList(List<QlnvBbanHaodoiDtl> details) {
        if (this.detailList == null) {
            this.detailList = details;
        } else if(this.detailList != details) {
            this.detailList.clear();
            for (QlnvBbanHaodoiDtl detail : details) {
            	detail.setHeader(this);
    		}
            if(details != null){
                this.detailList.addAll(details);
            }
        }
    }
	
	public QlnvBbanHaodoiHdr addDetail(QlnvBbanHaodoiDtl dt) {
		detailList.add(dt);
        dt.setHeader(this);
        return this;
    }
 
    public QlnvBbanHaodoiHdr removeDetail(QlnvBbanHaodoiDtl dt) {
    	detailList.remove(dt);
        dt.setHeader(null);
        return this;
    }
}
