package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhThDanhSachReq extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDanhSach;
  private LocalDate thoiGianTlTu;
  private LocalDate thoiGianTlDen;
  private String trangThai;
  private Long idHoSo;
  private String soHoSo;
  private Long idQdPd;
  private String soQdPd;
  private LocalDate ngayKyQd;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private BigDecimal tongSlHienTai;
  private BigDecimal tongSlDeXuat;
  private BigDecimal tongSlDaDuyet;

  private Long idHdr;
  private Long idTongHop;
  private String maTongHop;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private String donViTinh;
  private BigDecimal slHienTai;
  private BigDecimal slDeXuat;
  private BigDecimal slDaDuyet;
  private BigDecimal thanhTien;
  private LocalDate ngayNhapKho;
  private LocalDate ngayDeXuat;
  private LocalDate ngayDeXuatTu;
  private LocalDate ngayDeXuatDen;
  private LocalDate ngayTongHop;
  private String lyDo;
  private String type;

  private String dvql;

  private String maDviSr;
  private LocalDate thoiGianThTu;
  private LocalDate thoiGianThDen;
}
