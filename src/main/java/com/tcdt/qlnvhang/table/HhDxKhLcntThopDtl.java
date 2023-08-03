package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.DsChiCucPreview;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

@Entity
@Table(name = "HH_DX_KHLCNT_THOP_DTL")
@Data
public class HhDxKhLcntThopDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHLCNT_THOP_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_DX_KHLCNT_THOP_DTL_SEQ", allocationSize = 1, name = "HH_DX_KHLCNT_THOP_DTL_SEQ")
	private Long id;

	Long idThopHdr;
	Long idDxHdr;
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
	Long soGthau;
	String namKhoach;
	String trichYeu;
	String diaChiDvi;
	BigDecimal donGiaVat;

	@Transient
	private String tgianDongThau;
	@Transient
	private String soLuongStr;
	@Transient
	private List<DsChiCucPreview> dsChiCucPreviews;
}
