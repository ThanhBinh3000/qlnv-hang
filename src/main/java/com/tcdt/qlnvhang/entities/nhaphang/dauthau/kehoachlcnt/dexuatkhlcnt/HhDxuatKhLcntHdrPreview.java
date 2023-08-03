package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.report.HhDxKhlcntDsgthauReport;
import com.tcdt.qlnvhang.table.report.ListDsGthauDTO;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Data
public class HhDxuatKhLcntHdrPreview implements Serializable {

	private Long id;
	String soDxuat;
	String loaiVthh;
	String tenLoaiVthh;
	String tenNguonVon;
	String tenHthucLcnt;
	String soQd;
	String trichYeu;
	String maDvi;
	String tenDvi;
	String tenDviCha;
	String trangThai;
	String tenTrangThai;
	String tenTrangThaiTh;
	@Temporal(TemporalType.DATE)
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	String ldoTuchoi;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayPduyet;
	String nguoiPduyet;
	@Temporal(TemporalType.DATE)
	Date ngayKy;
	Integer namKhoach;
	String ghiChu;
	String cloaiVthh;
	String tenCloaiVthh;
	String moTaHangHoa;
	String tenDuAn;
	BigDecimal tongMucDt;
	BigDecimal tongMucDtDx;
	String loaiHdong;
	String tenLoaiHdong;
	String tchuanCluong;
	String nguonVon;
	String hthucLcnt;
	String pthucLcnt;
	String tenPthucLcnt;
	String tgianBdauTchuc;
	String tgianMthau;
	String tgianDthau;
	Integer tgianThien;
	String tgianNhang;
	Integer gtriDthau;
	Integer gtriHdong;
	String dienGiai;
	String trangThaiTh;
	String loaiHinhNx;
	String tenLoaiHinhNx;
	String kieuNx;
	String tenKieuNx;
	String diaChiDvi;
	BigDecimal donGiaVat;
	String soQdPdKqLcnt;
	Long idQdPdKqLcnt;
	Integer soGthauTrung;
	Integer tongSl;
	Integer tongSlChiTieu = 0;
	BigDecimal tongThanhTien;

	private ReportTemplateRequest reportTemplateRequest;
	private List<ListDsGthauDTO> listDsGthau;
	private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
	private List<HhDxKhlcntDsgthauReport> dsGtDtlList = new ArrayList<>();
	private Long soGoiThau;
	private List<HhDxuatKhLcntCcxdgDtl> ccXdgDtlList = new ArrayList<>();
	private Long maTh;
	private BigDecimal qdGiaoChiTieuId;
	String ykienThamGia;
	private List<HhDxKhlcntDsgthau> dsGtVt;

}
