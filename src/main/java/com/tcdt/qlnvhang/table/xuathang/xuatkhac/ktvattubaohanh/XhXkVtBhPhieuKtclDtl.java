package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhXkVtBhPhieuKtclDtl.TABLE_NAME)
@Data
public class XhXkVtBhPhieuKtclDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XK_VT_BH_PHIEU_KTCL_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBhPhieuKtclDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXkVtBhPhieuKtclDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhXkVtBhPhieuKtclDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private String tenChiTieu;
  private String maChiTieu;
  private String chiSo;
  private String ketQua;
  private String ppKiemTra;
  private Integer danhGia; // Đạt/ Không đạt

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhXkVtBhPhieuKtclHdr phieuKtclHdr;
}
