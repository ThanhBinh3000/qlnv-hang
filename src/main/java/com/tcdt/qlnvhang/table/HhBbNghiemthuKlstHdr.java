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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinKeLot;

import lombok.Data;

@Entity
@Table(name = HhBbNghiemthuKlstHdr.TABLE_NAME)
@Data
public class HhBbNghiemthuKlstHdr extends TrangThaiBaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "NH_BB_NGHIEM_THU";

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_NGHIEM_THU_SEQ")
//	@SequenceGenerator(sequenceName = "BB_NGHIEM_THU_SEQ", allocationSize = 1, name = "BB_NGHIEM_THU_SEQ")
	private Long id;
//	private Long qdgnvnxId; // HhQdGiaoNvuNhapxuatHdr
	String soBbNtBq;
	@Temporal(TemporalType.DATE)
	Date ngayNghiemThu;
	Long idThuTruong;
	Long idKeToan;
	Long idKyThuatVien;
	Long idThuKho;

	String lhKho;
	Double slThucNhap;
	Double tichLuong;
	String pthucBquan;
	String hthucBquan;
	Double dinhMuc;
	String ketLuan;
	String trangThai;
	String ldoTuchoi;
	String capDvi;
	String maDvi;
	Integer nam;
	String loaiVthh;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	String moTaHangHoa;
	String maVatTu;
	String maVatTuCha;


	private Integer so;

	private Long hopDongId;

	@Transient
	String soHopDong;

	@Transient
	String tenDvi;

	String maDiemKho;
	@Transient
	String tenDiemKho;

	String maNhaKho;
	@Transient
	String tenNhaKho;

	String maNganKho;
	@Transient
	String tenNganKho;

	String maLoKho;
	@Transient
	String tenLoKho;

	@Transient
	BigDecimal chiPhiThucHienTrongNam;

	@Transient
	BigDecimal chiPhiThucHienNamTruoc;

	@Transient
	BigDecimal tongGiaTri;

	@Transient
	String tenTrangThai;

	@Transient
	String trangThaiDuyet;

	@Transient
	String tongGiaTriBangChu;

	@Transient
	String soQuyetDinhNhap;

	@Transient
	String tenVatTu;

	@Transient
	String tenVatTuCha;

	@Transient
	String maQhns;

	Long idDdiemGiaoNvNh;

	Long idQdGiaoNvNh;

	String soQdGiaoNvNh;

	String soPhieuNhapKho;


	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhBbNghiemthuKlstDtl> children = new ArrayList<>();

	public void setChildren(List<HhBbNghiemthuKlstDtl> children) {
		this.children.clear();
		for (HhBbNghiemthuKlstDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(HhBbNghiemthuKlstDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhBbNghiemthuKlstHdr.TABLE_NAME + "'")
	private List<FileDKemJoinKeLot> children1 = new ArrayList<>();

	public void setChildren1(List<FileDKemJoinKeLot> children1) {
		this.children1.clear();
		for (FileDKemJoinKeLot child1 : children1) {
			child1.setParent(this);
		}
		this.children1.addAll(children1);
	}

	public void addChild1(FileDKemJoinKeLot child1) {
		child1.setParent(this);
		this.children1.add(child1);
	}
}
