package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinHhDchinhDxKhLcntHdr;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "HH_DC_DX_LCNT_HDR")
@Data
public class HhDchinhDxKhLcntHdr extends TrangThaiBaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_LCNT_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_LCNT_HDR_SEQ", allocationSize = 1, name = "QLNV_QD_LCNT_HDR_SEQ")
	private Long id;

	String soQdDc;

	String soQdGoc;
	String soTtrDc;

	Long idQdGoc;

	Integer nam;

	@Temporal(TemporalType.DATE)
	Date ngayQd;
	@Temporal(TemporalType.DATE)
	Date ngayQdDc;

	String loaiVthh;

	String cloaiVthh;

	String moTaHangHoa;

	String trichYeu;

	@Temporal(TemporalType.DATE)
	Date ngayHluc;

	String loaiDieuChinh;

	String hthucLcnt;

	String pthucLcnt;

	String loaiHdong;

	String nguonVon;
	String maDvi;
	@Transient
	String tenDvi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianBdauTchuc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianDthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNhang;

	Integer tgianThien;

	Integer gtriDthau;

	Integer gtriHdong;
	private Boolean lastest;
	private Integer lanDieuChinh;
	String noiDungQd;
	String noiDungTtr;
	String trichYeuQd;
	String tenDuAn;
	String dienGiaiTongMucDt;
	String dienGiai;
	String quyMo;
	BigDecimal tongMucDtDx;
	BigDecimal tongMucDt;
	Integer tgianThienHd;
	Integer vat;
	@Transient
	Integer gthauTrung;
	@Transient
	Integer soHdKy;
	@Transient
	Integer tgianNhapKho;

	@Transient
	private List<HhDchinhDxKhLcntDtl> listDieuChinh = new ArrayList<>();
	@Transient
	private List<HhDchinhDxKhLcntDsgthau> dsGthau = new ArrayList<>();

	@Transient
	private List<HhDchinhDxKhLcntDtl> children = new ArrayList<>();

	@Transient
	private Long soGoiThau;

	@Transient
	String tenLoaiVthh;

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
	@Transient
	private List<FileDinhKem> listCcPhapLy;
	@Transient
	private List<FileDinhKem> fileDinhKems;
	@Transient
	private List<FileDinhKem> fileDinhKemsTtr;
}
