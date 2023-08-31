package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import lombok.Data;

@Data
public class XhXuatCapPhieuKnClDtlReq {
  private Long id;
  private Long idHdr;
  private String chiSoNhap;
  private String chiSoXuat;
  private String danhMuc;
  private String phuongPhap;
  private String tenTchuan;
  private String maChiTieu;
  private String trangThai;
  private String ketQuaPt;
  private Long thuTu;
}
