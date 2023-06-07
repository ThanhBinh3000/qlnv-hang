package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdNhapHangKhac;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan.NhBbGiaoNhanVt;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = NhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME)
@Data
public class NhQdGiaoNvuNhapHangKhacHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "NH_QD_GIAO_NVU_NHAPXUAT_KHAC";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_NHAPXUAT_KHAC_SEQ")
	@SequenceGenerator(sequenceName = "QD_GIAO_NVU_NHAPXUAT_KHAC_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_NHAPXUAT_KHAC_SEQ")
	private Long id;

	String soQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdinh;

	String maDvi;

	String loaiQd;

	String trangThai;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayTao;
	String nguoiTao;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngaySua;
	String nguoiSua;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;

	String ldoTuchoi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayPduyet;
	String nguoiPduyet;

	String ghiChu;

	String capDvi;

	String loaiVthh;

	String cloaiVthh;

	String trichYeu;

	Integer namNhap;

	Long idHd;

	String soHd;

	String tenGoiThau;

	String donViTinh;

	Long soLuong;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNkho;

	String moTaHangHoa;

	@Transient
	String tenDvi;

	@Transient
	String tenLoaiQd;

	@Transient
	String tenTrangThai;

	@Transient
	String trangThaiDuyet;

	@Transient
	String tenLoaiVthh;

	@Transient
	String tenCloaiVthh;

	@Transient
	private List<NhQdGiaoNvuNhapHangKhacDtl> dtlList = new ArrayList<>();

	@Transient
	private HhHopDongHdr hopDong;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + NhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME + "'")
	private List<FileDKemJoinQdNhapHangKhac> children2 = new ArrayList<>();

	public void setChildren2(List<FileDKemJoinQdNhapHangKhac> children2) {
		this.children2.clear();
		for (FileDKemJoinQdNhapHangKhac child2 : children2) {
			child2.setParent(this);
		}
		this.children2.addAll(children2);
	}

	public void addChild2(FileDKemJoinQdNhapHangKhac child2) {
		child2.setParent(this);
		this.children2.add(child2);
	}

	@Transient
	List<PhieuKnghiemCluongHang> listPhieuKiemNghiemCl;

	@Transient
	List<NhBbGiaoNhanVt> listBienBanGiaoNhan;
	@Transient
	private List<FileDinhKem> fileDinhKems;
	@Transient
	private List<FileDinhKem> fileCanCu;
}
