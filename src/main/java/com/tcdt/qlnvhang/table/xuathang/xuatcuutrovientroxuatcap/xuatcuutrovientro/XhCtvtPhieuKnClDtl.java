package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhCtvtPhieuKnClDtl.TABLE_NAME)
@Data
public class XhCtvtPhieuKnClDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_PHIEU_KN_CL_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtPhieuKnClDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtPhieuKnClDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtPhieuKnClDtl.TABLE_NAME + "_SEQ")
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
