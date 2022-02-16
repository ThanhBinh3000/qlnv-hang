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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "yyyy-MM-dd")
	//@DateTimeFormat(pattern = "yyyy-MM-dd")
	//@Temporal(TemporalType.DATE)
	Date ngayVban;
	String trangThai;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date ngayTao;
	String nguoiTao;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date ngaySua;
	String nguoiSua;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date ngayPduyet;
	String nguoiPduyet;
	String ldoTuchoi;
	String maDvi;
	String nguonvon;
	String hanghoa;
	String soDx;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date ngayDx;
	String tenGoithau;
	BigDecimal tongTien;
	Integer soLuong;
	String donVi;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date ngayThienTu;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date ngayThienDen;
	String tcChatLuong;
	Integer soPhanThau;
	String hthucLcnt;
	String pthucLcnt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date ngayPhanh;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date thoiHanNhap;
	BigDecimal giaBan;
	BigDecimal tienBaoLanh;
	BigDecimal tienDamBao;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	Date ngayDongThau;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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
