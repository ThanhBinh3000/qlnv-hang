package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = XhTcTtinBdgTaiSan.TABLE_NAME)
public class XhTcTtinBdgTaiSan extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TC_TTIN_BDG_TAI_SAN";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TC_TTIN_BDG_TAI_SAN_SEQ")
  @SequenceGenerator(sequenceName = "XH_TC_TTIN_BDG_TAI_SAN_SEQ", allocationSize = 1, name = "XH_TC_TTIN_BDG_TAI_SAN_SEQ")
  private Long id;
  private Long idTtinHdr;
  private Long idTtinDtl;
  private String maDvi;
  private String maDiaDiem;
  private Long soLuong;
  private Long donGia;
  private Long donGiaCaoNhat;
  private String cloaiVthh;
  private String maDvTaiSan;
  private Long tonKho;
  private String donViTinh;
  private Long giaKhoiDiem;
  private Long soTienDatTruoc;
  private int soLanTraGia;
  private String nguoiTraGiaCaoNhat;
  @Transient
  private String tenDvi;
  @Transient
  private String tenChiCuc;
  @Transient
  private String tenDiemKho;
  @Transient
  private String tenNhaKho;
  @Transient
  private String tenNganKho;
  @Transient
  private String tenLoKho;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
}
