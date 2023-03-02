package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhXuatCapPhieuKnClDtl.TABLE_NAME)
@Data
public class XhXuatCapPhieuKnClDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XUAT_CAP_PHIEU_KN_CL_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXuatCapPhieuKnClDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXuatCapPhieuKnClDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhXuatCapPhieuKnClDtl.TABLE_NAME + "_SEQ")
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
}
