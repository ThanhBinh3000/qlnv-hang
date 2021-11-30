package com.tcdt.qlnvhang.table;

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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QLNV_KHOACH_LCNT_HDR")
@Data
public class QlnvKhoachLcntHdr {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_KHOACH_LCNT_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_KHOACH_LCNT_HDR_SEQ", allocationSize = 1, name = "QLNV_KHOACH_LCNT_HDR_SEQ")
	private Long id;

	String loaiHanghoa;
	String soQdGiaoCtkh;
	String soKhDtoan;
	String soVban;
	Date ngayVban;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	Date ngayPduyet;
	String nguoiPduyet;
	String ldoTuchoi;
	String maDvi;
	String nguonvon;
	String hanghoa;
	String soDx;
	String ngayDx;
	String tenGoiThau;
	BigDecimal tongTien;
	Integer soLuong;
	String donVi;
	Date ngayThienTu;
	Date ngayThienDen;
	String tcChatLuong;
	Integer soPhanThau;
	String hthucLcnt;
	String pthucLcnt;
	Date ngayPhanh;
	Date thoiHanNhap;
	BigDecimal giaBan;
	BigDecimal tienBaoLanh;
	BigDecimal tienDamBao;
	Date ngayDongThau;
	Date ngayMoHso;
	String loaiHdong;
	BigDecimal giaTtinh;
	String kienNghi;
	String trangThaiTh;
	@OneToMany(
	        mappedBy = "header",
	        		fetch = FetchType.LAZY, cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	private List<QlnvKhoachLcntDtl> detailList = new ArrayList<QlnvKhoachLcntDtl>();
	
	public void setDetailList(List<QlnvKhoachLcntDtl> detail) {
        if (this.detailList == null) {
            this.detailList = detail;
        } else if(this.detailList != detail) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.detailList.clear();
            if(detail != null){
                this.detailList.addAll(detail);
            }
        }
    }
	
	public QlnvKhoachLcntHdr addDetail(QlnvKhoachLcntDtl dt) {
		detailList.add(dt);
        dt.setHeader(this);
        return this;
    }
 
    public QlnvKhoachLcntHdr removeDetail(QlnvKhoachLcntDtl dt) {
    	detailList.remove(dt);
        dt.setHeader(null);
        return this;
    }
}
