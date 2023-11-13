package com.tcdt.qlnvhang.entities.khcn.quychuankythuat;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = QuyChuanQuocGiaDtl.TABLE_NAME)
@Data
public class QuyChuanQuocGiaDtl extends BaseEntity {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "KHCN_QUY_CHUAN_QG_DTL";
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = QuyChuanQuocGiaDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = QuyChuanQuocGiaDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = QuyChuanQuocGiaDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHdr;
  private String maChiTieu;
  private String tenChiTieu;
  private String thuTuHt;
  private boolean chiTieuCha;
  private String maDvi;
  private String mucYeuCauNhap;
  private String mucYeuCauNhapToiDa;
  private String mucYeuCauNhapToiThieu;
  private String mucYeuCauXuat;
  private String mucYeuCauXuatToiDa;
  private String mucYeuCauXuatToiThieu;
  private String phuongPhapXd;
  private String loaiVthh;
  private String cloaiVthh;
  private String ghiChu;
  private String nhomCtieu;
  private String toanTu;

  @Transient
  private String tenDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenLoaiHinhNhapXuat;
  @Transient
  private FileDinhKem fileDinhKem;
}
