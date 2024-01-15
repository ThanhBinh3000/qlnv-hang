package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import lombok.Data;

@Data
public class XhXuatCapPhieuKtClDtlReq {
  private Long id;
  private Long idHdr;
  private String chiSoNhap;
  private String chiSoXuat;
  private String danhMuc;
  private String phuongPhap;
  private String tenTchuan;
  private String trangThai;
  private String ketQuaPt;
  private Long thuTu;
  private String chiSoClToiThieu;
  private String chiSoClToiDa;
  private String toanTu;
  private String maChiTieu;
  private String danhGia;
}
