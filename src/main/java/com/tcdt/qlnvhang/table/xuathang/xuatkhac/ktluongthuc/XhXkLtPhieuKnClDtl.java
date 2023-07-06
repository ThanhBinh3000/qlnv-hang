package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhXkLtPhieuKnClDtl.TABLE_NAME)
@Data
public class XhXkLtPhieuKnClDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XK_LT_PHIEU_KN_CL_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkLtPhieuKnClDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXkLtPhieuKnClDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhXkLtPhieuKnClDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private String chiSoNhap;
  private String chiSoXuat;
  private String danhMuc;
  private String phuongPhap;
  private String tenTchuan;
  private String trangThai;
  private String ketQuaPt;
  private Long thuTu;
  private String danhGia;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="idHdr")
  @JsonIgnore
  private XhXkLtPhieuKnClHdr phieuKnClHdr;
}
