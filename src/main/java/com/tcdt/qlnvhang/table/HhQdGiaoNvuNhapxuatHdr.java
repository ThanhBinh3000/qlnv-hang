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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuKiemTraChatLuong;
import com.tcdt.qlnvhang.util.Contains;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdNhapxuat;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = HhQdGiaoNvuNhapxuatHdr.TABLE_NAME)
@Data
public class HhQdGiaoNvuNhapxuatHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "NH_QD_GIAO_NVU_NHAPXUAT";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_NHAPXUAT_SEQ")
	@SequenceGenerator(sequenceName = "QD_GIAO_NVU_NHAPXUAT_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_NHAPXUAT_SEQ")
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
	List<QlpktclhPhieuKtChatLuong> listPhieuKtraCl = new ArrayList<>();

	@Transient
	private List<HhQdGiaoNvuNhapxuatDtl> dtlList = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhQdGiaoNvuNhapxuatHdr.TABLE_NAME + "'")
	private List<FileDKemJoinQdNhapxuat> children2 = new ArrayList<>();

	public void setChildren2(List<FileDKemJoinQdNhapxuat> children2) {
		this.children2.clear();
		for (FileDKemJoinQdNhapxuat child2 : children2) {
			child2.setParent(this);
		}
		this.children2.addAll(children2);
	}

	public void addChild2(FileDKemJoinQdNhapxuat child2) {
		child2.setParent(this);
		this.children2.add(child2);
	}
}
