package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtvtBbTinhKhoHdrReq {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soBbTinhKho;
  private LocalDate ngayTaoBb;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private Long idPhieuXuatKho;
  private String soPhieuXuatKho;
  private LocalDate ngayXuatKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private String idBkCanHang;
  private String soBkCanHang;
  private LocalDate ngayBatDauXuat;
  private LocalDate ngayKetThucXuat;
  private BigDecimal tongSlNhap;
  private BigDecimal tongSlXuat;
  private BigDecimal slConLai;
  private BigDecimal slThucTeCon;
  private BigDecimal slThua;
  private BigDecimal slThieu;
  private String nguyenNhan;
  private String kienNghi;
  private String ghiChu;
  private String thuKho;
  private String ktvBaoQuan;
  private String keToan;
  private String ldChiCuc;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
  private List<XhCtvtBbTinhKhoDtlReq> listPhieuXuatKho= new ArrayList<>();
}
