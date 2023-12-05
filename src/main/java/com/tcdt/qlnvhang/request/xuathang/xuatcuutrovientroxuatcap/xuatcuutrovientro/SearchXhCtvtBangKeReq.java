package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SearchXhCtvtBangKeReq extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String dvql;
  private String maQhNs;
  private String soBangKe;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private String idPhieuXuatKho;
  private String ngayXuat;
  private String diaDiemKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private String nlqHoTen;
  private String nlqCmnd;
  private String nlqDonVi;
  private String nlqDiaChi;
  private LocalDate thoiGianGiaoNhan;
  private BigDecimal tongTrongLuong;
  private BigDecimal tongTrongLuongBaoBi;
  private BigDecimal tongTrongLuongHang;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  private LocalDate ngayXuatTu;
  private LocalDate ngayXuatDen;
  private LocalDate thoiGianGiaoNhanTu;
  private LocalDate thoiGianGiaoNhanDen;
}
