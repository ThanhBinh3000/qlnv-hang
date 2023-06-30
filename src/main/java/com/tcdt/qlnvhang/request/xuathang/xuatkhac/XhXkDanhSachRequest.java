package com.tcdt.qlnvhang.request.xuathang.xuatkhac;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhXkDanhSachRequest extends BaseRequest {
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
  private BigDecimal tongSlHetHan;

  private Long idHdr;
  private Long idTongHop;
  private String maTongHop;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private String donViTinh;
  private BigDecimal slHetHan;
  private LocalDate ngayNhapKho;
  private LocalDate ngayDeXuat;
  private LocalDate ngayDeXuatTu;
  private LocalDate ngayDeXuatDen;
  private LocalDate ngayTongHop;
  private String lyDo;
  private String loai;

  private String dvql;
}
