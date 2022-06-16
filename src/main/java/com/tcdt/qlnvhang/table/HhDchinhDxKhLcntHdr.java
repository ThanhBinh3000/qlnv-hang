package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import lombok.Data;

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

}
