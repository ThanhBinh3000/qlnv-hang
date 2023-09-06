package com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = XhDgBangKeHdr.TABLE_NAME)
@Data
public class XhDgBangKeHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_DG_BANG_KE_HDR";

  @Id
  /*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDgBangKeHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhDgBangKeHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhDgBangKeHdr.TABLE_NAME + "_SEQ")*/
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhns;
  private String soBangKe;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private Long idHdong;
  private String soHdong;
  private LocalDate ngayKyHd;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private Long idPhieuXuatKho;
  private String soPhieuXuatKho;
  private LocalDate ngayXuat;
  private String diaDiemKho;
  private String nlqHoTen;
  private String nlqCmnd;
  private String nlqDonVi;
  private String nlqDiaChi;
  private LocalDate thoiGianGiaoNhan;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private String donViTinh;
  private Long tongTrongLuongBaoBi;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String trangThai;

// Transient
  @Transient
  private String tenDvi;
  @Transient
  private String diaChiDvi;
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
  @Transient
  private String tenTrangThai;
  @Transient
  private String nguoiPduyet;
  @Transient
  private String nguoiGduyet;
  @Transient
  private BigDecimal trongLuongCaBi;
  @Transient
  private List<XhDgBangKeDtl> bangKeDtl = new ArrayList<>();

  public void setBangKeDtl(List<XhDgBangKeDtl> bangKeDtl) {
    this.bangKeDtl = bangKeDtl;
    if (!DataUtils.isNullOrEmpty(bangKeDtl)) {
      this.trongLuongCaBi = bangKeDtl.stream().map(XhDgBangKeDtl::getTrongLuongCaBi).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
  }
}
