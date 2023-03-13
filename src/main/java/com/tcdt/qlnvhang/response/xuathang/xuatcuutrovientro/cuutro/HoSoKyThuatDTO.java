package com.tcdt.qlnvhang.response.xuathang.xuatcuutrovientro.cuutro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class HoSoKyThuatDTO {
  private Long id;

  private Long idQdGiaoNvNh;

  private String soQdGiaoNvNh;

  private String soBbLayMau;

  private String soHd;

  private String maDvi;

  private String soHoSoKyThuat;

  private Integer nam;

  private Integer idBbLayMauXuat;

  private Boolean kqKiemTra;

  private String loaiNhap;

  private String maDiemKho;

  private String maNhaKho;

  private String maNganKho;

  private String maLoKho;

  private String tenDvi;

  @Temporal(TemporalType.DATE)
  private Date ngayTao;

  public HoSoKyThuatDTO(Long id, Long idQdGiaoNvNh, String soQdGiaoNvNh, String soBbLayMau, String soHd, String maDvi, String soHoSoKyThuat, Integer nam, Integer idBbLayMauXuat, Boolean kqKiemTra, String loaiNhap, String maDiemKho, String maNhaKho, String maNganKho, String maLoKho, Date ngayTao) {
    this.id = id;
    this.idQdGiaoNvNh = idQdGiaoNvNh;
    this.soQdGiaoNvNh = soQdGiaoNvNh;
    this.soBbLayMau = soBbLayMau;
    this.soHd = soHd;
    this.maDvi = maDvi;
    this.soHoSoKyThuat = soHoSoKyThuat;
    this.nam = nam;
    this.idBbLayMauXuat = idBbLayMauXuat;
    this.kqKiemTra = kqKiemTra;
    this.loaiNhap = loaiNhap;
    this.maDiemKho = maDiemKho;
    this.maNhaKho = maNhaKho;
    this.maNganKho = maNganKho;
    this.maLoKho = maLoKho;
    this.ngayTao = ngayTao;
  }
}
