package com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhPhuLucHd;

import lombok.Data;

@Entity
@Table(name = HhHopDongHdr.TABLE_NAME)
@Data
public class HhHopDongHdr extends BaseEntity implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_HOP_DONG_HDR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhHopDongHdr.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhHopDongHdr.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhHopDongHdr.TABLE_NAME + "_SEQ")
	private Long id;
	private Integer namHd;
	private String soQdKqLcnt;
	@Temporal(TemporalType.DATE)
	Date ngayQdKqLcnt;
	private Long idQdKqLcnt;
	private String soQdPdKhlcnt;
	private String tenGoiThau;
	private Long idGoiThau;
	@Temporal(TemporalType.DATE)
	Date tgianNkho;
	String soHd;
	String tenHd;
	@Temporal(TemporalType.DATE)
	Date ngayKy;
	String ghiChuNgayKy;
	String loaiHdong;
	String ghiChuLoaiHdong;
	Integer soNgayThien;
	Integer soNgayThienHd;
	Integer tgianBhanh;

	String maDvi;
	@Transient
	String tenDvi;
	String diaChi;
	String mst;
	String tenNguoiDdien;
	Long idNguoiDdien;
	String fax;
	String moTai;
	String giayUyQuyen;
	String chucVu;
	String sdt;
	String stk;

	String tenNhaThau;
	String diaChiNhaThau;
	String mstNhaThau;
	String tenNguoiDdienNhaThau;
	String chucVuNhaThau;
	String sdtNhaThau;
	String stkNhaThau;
	String faxNhaThau;
	String moTaiNhaThau;
	String giayUyQuyenNhaThau;

	String loaiVthh;
	@Transient
	String tenLoaiVthh;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	String moTaHangHoa;

	Double soLuong;
	Double donGia;

	String trangThai;
	@Transient
	String tenTrangThai;

	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	Date ngayPduyet;
	String nguoiPduyet;
	String ghiChu;
	String namKhoach;

	String donViTinh;

	String noiDung;

	String dieuKien;
	@Temporal(TemporalType.DATE)
	private Date ngayHlucHd;
	@Temporal(TemporalType.DATE)
	private Date tgianBdamThienHd;
	@Temporal(TemporalType.DATE)
	private Date tgianGiaoThucTe;

	@Transient
	private List<HhHopDongDtl> details;

	@Transient
	private List<HhPhuLucHd> hhPhuLucHdongList = new ArrayList<>();
	@Transient
	private List<FileDinhKem> listFileDinhKem;
	@Transient
	private List<FileDinhKem> listCcPhapLy;
}
