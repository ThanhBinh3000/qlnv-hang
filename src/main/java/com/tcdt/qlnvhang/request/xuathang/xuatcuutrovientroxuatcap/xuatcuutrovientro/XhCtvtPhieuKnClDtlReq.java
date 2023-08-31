package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import lombok.Data;

@Data
public class XhCtvtPhieuKnClDtlReq {
  private Long id;
  private Long idHdr;
  private String chiSoNhap;
  private String chiSoXuat;
  private String danhMuc;
  private String phuongPhap;
  private String tenTchuan;
  private String maChiTieu;
  private String trangThai;
  private Long thuTu;
  private String ketQuaPt;
}
