package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhXuatCapPhieuKnClDtl.TABLE_NAME)
@Data
public class XhXuatCapPhieuKnClDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XC_PHIEU_KN_CL_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_XC_PHIEU_KN_CL_DTL_SEQ")
  @SequenceGenerator(sequenceName = "XH_XC_PHIEU_KN_CL_DTL_SEQ", allocationSize = 1, name =  "XH_XC_PHIEU_KN_CL_DTL_SEQ")
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
