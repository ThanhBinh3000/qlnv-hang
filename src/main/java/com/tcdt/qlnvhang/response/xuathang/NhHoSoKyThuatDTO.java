package com.tcdt.qlnvhang.response.xuathang;

import com.tcdt.qlnvhang.util.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class NhHoSoKyThuatDTO {
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
  private String tenDiemKho;
  private String tenNhaKho;
  private String tenNganKho;
  private String tenLoKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenLoaiVthh;
  private String tenCloaiVthh;
  private String trangThai;
  private String tenTrangThai;
  private String tenDvi;
  private LocalDate ngayTao;
  private String soBbanKtraNquan;
  private String soBbanKtraVhanh;
  private String soBbanKtraHskt;

  public NhHoSoKyThuatDTO(Long id, Long idQdGiaoNvNh, String soQdGiaoNvNh, String soBbLayMau, String soHd, String maDvi, String soHoSoKyThuat, Integer nam, Integer idBbLayMauXuat, Boolean kqKiemTra, String loaiNhap, String maDiemKho, String maNhaKho, String maNganKho, String maLoKho, Date ngayTao) {
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
    this.ngayTao = DataUtils.convertToLocalDate(ngayTao);
  }
}
