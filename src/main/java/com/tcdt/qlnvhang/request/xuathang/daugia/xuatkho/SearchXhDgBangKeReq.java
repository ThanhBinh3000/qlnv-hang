package com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchXhDgBangKeReq extends BaseRequest {
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
  private String soPhieuXuatKho;
  private String ngayXuat;
  private String ngayXuatTu;
  private String ngayXuatDen;
  private String diaDiemKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private String nlqHoTen;
  private String nlqCmnd;
  private String nlqDonVi;
  private String nlqDiaChi;
  private LocalDate thoiGianGiaoNhan;
  private Long tongTrongLuong;
  private Long tongTrongLuongBaoBi;
  private Long tongTrongLuongHang;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
}
