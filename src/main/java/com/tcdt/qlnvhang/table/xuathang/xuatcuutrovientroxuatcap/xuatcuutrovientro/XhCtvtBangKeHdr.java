package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtvtBangKeHdr.TABLE_NAME)
@Data
public class XhCtvtBangKeHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_BANG_KE_HDR";

  @Id
  /*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtBangKeHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtBangKeHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtBangKeHdr.TABLE_NAME + "_SEQ")*/
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhns;
  private String soBangKe;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private String maKho;
  private Long idPhieuXuatKho;
  private String soPhieuXuatKho;
  private LocalDate ngayXuat;
  private String diaDiemKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String thuKho;
  private String nlqHoTen;
  private String nlqCmnd;
  private String nlqDonVi;
  private String nlqDiaChi;
  private LocalDate thoiGianGiaoNhan;
  private Long tongTrongLuong;
  private Long tongTrongLuongBaoBi;
  private Long tongTrongLuongHang;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String trangThai;
  private String type;

  @Transient
  private String tenDvi;
  @Transient
  private String diaChiDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
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
  private String nguoiPduyet;
  @Transient
  private String nguoiGduyet;
  @Transient
  private String tenKho;
  @Transient
  private List<XhCtvtBangKeDtl> bangKeDtl = new ArrayList<>();

  public String getMaKho() {
    return DataUtils.isNullOrEmpty(maLoKho) ? maNganKho : maLoKho;
  }

  public String getTenKho() {
    return DataUtils.isNullOrEmpty(tenLoKho) ? tenNganKho : tenLoKho;
  }
}
