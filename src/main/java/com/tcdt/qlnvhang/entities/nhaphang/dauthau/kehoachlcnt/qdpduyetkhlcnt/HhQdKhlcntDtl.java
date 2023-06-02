package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

@Entity
@Table(name = "HH_QD_KHLCNT_DTL")
@Data
public class HhQdKhlcntDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_KHLCNT_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_QD_KHLCNT_DTL_SEQ", allocationSize = 1, name = "HH_QD_KHLCNT_DTL_SEQ")
	private Long id;
	private Long idQdHdr;
	String maDvi;
	@Transient
	String tenDvi;
	String soDxuat;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayTao;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayPduyet;
	String tenDuAn;
	BigDecimal soLuong;
	BigDecimal donGiaVat;
	BigDecimal donGiaTamTinh;

	String goiThau;

	String cloaiVthh;
	@Transient
	String tenCloaiVthh;

	String loaiVthh;
	@Transient
	String tenLoaiVthh;

	Long soGthau;

	@Transient
	Long soGthauTrung;

	@Transient
	Long soGthauTruot;
	String namKhoach;
	Long idDxHdr;
	String trangThai;
	@Transient
	String tenTrangThai;
	String diaChiDvi;
	String trichYeu;

	@Column(name="SO_QD_PD_KQ_LCNT")
	String soQdPdKqLcnt;

	String tenNhaThau;

	BigDecimal donGiaNhaThau;

	Long idNhaThau;
	Long idDcDxHdr;

	@Transient
	private HhQdKhlcntHdr hhQdKhlcntHdr;

	@Transient
	private HhDxuatKhLcntHdr dxuatKhLcntHdr;
	@Transient
	private HhDxKhLcntThopHdr hhDxKhLcntThopHdr;

	@Transient
	private HhQdPduyetKqlcntHdr hhQdPduyetKqlcntHdr;

	@Transient
	private List<HhQdKhlcntDsgthau> children = new ArrayList<>();

}
