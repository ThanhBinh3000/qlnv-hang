package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinHhDchinhDxKhLcntHdr;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdKhlcntHdr;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "HH_DC_DX_LCNT_HDR")
@Data
public class HhDchinhDxKhLcntHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_LCNT_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_LCNT_HDR_SEQ", allocationSize = 1, name = "QLNV_QD_LCNT_HDR_SEQ")
	private Long id;
	Long namKh;
	String soQdinh;
	String soQdinhGoc;

	@Temporal(TemporalType.DATE)
	Date ngayQd;
	String loaiVthh;
	String cloaiVthh;
	String trichYeu;
	@Temporal(TemporalType.DATE)
	Date ngayHluc;
	String trangThai;

	Date ngayTao;
	String nguoiTao;
	@Temporal(TemporalType.DATE)
	Date ngaySua;
	String nguoiSua;
	String ldoTuchoi;

	@Temporal(TemporalType.DATE)
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	@Temporal(TemporalType.DATE)
	Date ngayPduyet;
	String nguoiPduyet;
	String loaiDieuChinh;

	String hthucLcnt;
	String pthucLcnt;
	String loaiHdong;
	String nguonVon;
	@Temporal(TemporalType.DATE)
	Date tgianBdau;
	@Temporal(TemporalType.DATE)
	Date tgianDthau;
	@Temporal(TemporalType.DATE)
	Date tgianMthau;
	@Temporal(TemporalType.DATE)
	Date tgianNhang;
	String ghiChu;

	@Transient
	private List<HhDchinhDxKhLcntDtl> listDieuChinh = new ArrayList<>();

	String soQd;

	String soQdGoc;

	Long idQdGoc;

	@Transient
	String tenVthh;

	@Transient
	String tenCloaiVthh;

	@Transient
	String tenHthucLcnt;

	@Transient
	String tenPthucLcnt;

	@Transient
	String tenLoaiHdong;

	@Transient
	String tenNguonVon;

	@Temporal(TemporalType.DATE)
	Date tgianBdauTchuc;

	Long namKhoach;

	Integer tgianThienHd;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhQdKhlcntHdr.TABLE_NAME + "'")
	private List<FileDKemJoinHhDchinhDxKhLcntHdr> fileDinhKem = new ArrayList<>();

	public void setFileDinhKem(List<FileDKemJoinHhDchinhDxKhLcntHdr> children) {
		this.fileDinhKem.clear();
		for (FileDKemJoinHhDchinhDxKhLcntHdr child : children) {
			child.setParent(this);
		}
		this.fileDinhKem.addAll(children);
	}

	@Transient
	private List<HhDchinhDxKhLcntDtl> hhQdKhlcntDtlList = new ArrayList<>();

}
