package com.tcdt.qlnvhang.table;

import java.io.Serializable;
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

	Long idQdGoc;

	Integer nam;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;

	String loaiVthh;

	String cloaiVthh;

	String moTaHangHoa;

	String trichYeu;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
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
	@Transient
	Integer gthauTrung;
	@Transient
	Integer soHdKy;
	@Transient
	Integer tgianNhapKho;

	@Transient
	private List<HhDchinhDxKhLcntDtl> listDieuChinh = new ArrayList<>();

	@Transient
	private List<FileDKemJoinHhDchinhDxKhLcntHdr> fileDinhKem = new ArrayList<>();

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

}
