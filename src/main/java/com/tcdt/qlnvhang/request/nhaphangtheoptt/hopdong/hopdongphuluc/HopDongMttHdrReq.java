package com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMtt;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HopDongMttHdrReq extends BaseRequest {
  private Long id;
  private Long idHd;
  private Integer nam;
  private Long idQdPdKq;
  private String soQdPdKq;
  private Long idQdPdKh;
  private String soQdPdKh;
  LocalDate ngayKyQdPdKh;
  LocalDate tgianNkho;
  private String soHd;
  private String tenHd;
  LocalDate ngayKy;
  private String ngayKyGhiChu;
  private String loaiHdong;
  private String loaiHdongGhiChu;
  private Integer soNgayThien;
  LocalDate tgianGnhanTu;
  LocalDate tgianGnhanDen;
  LocalDate tgianGnhanGhiChu;
  private String noiDung;
  private String dieuKien;
  private String maDvi;
  private String cdtTen;
  private String cdtDiaChi;
  private String cdtMst;
  private String cdtTenNguoiDdien;
  private String cdtChucVu;
  private String cdtSdt;
  private String cdtFax;
  private String cdtStk;
  private String cdtMoTai;
  private String cdtThongTinGiayUyQuyen;
  private String nccTen;
  private String nccDiaChi;
  private String nccMst;
  private String nccTenNguoiDdien;
  private String nccChucVu;
  private String nccSdt;
  private String nccFax;
  private String nccStk;
  private String nccMoTai;
  private String nccThongTinGiayUyQuyen;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private String donViTinh;
  private BigDecimal soLuong;
  private BigDecimal donGia;
  private BigDecimal donGiaVat;
  private BigDecimal thanhTien;
  private String thanhTienBangChu;
  private String ghiChu;
  private List<Long> ids;
  private String dvql;
  private String nguoiKy;
  private String trangThaiNh;
  @Transient
  private LocalDate ngayKyTu;
  @Transient
  private LocalDate ngayKyDen;

  private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
  private List<FileDinhKemReq> canCu = new ArrayList<>();
  private List<DiaDiemGiaoNhanMttReq> diaDiemGiaoNhan = new ArrayList<>();
  private List<HopDongMttHdrReq> phuLuc = new ArrayList<>();
}
