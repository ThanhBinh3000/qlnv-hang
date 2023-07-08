package com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho;


import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhDgBbHaoDoiHdr.TABLE_NAME)
@Data
public class XhDgBbHaoDoiHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_DG_BB_HAO_DOI_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDgBbHaoDoiHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhDgBbHaoDoiHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhDgBbHaoDoiHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhns;
  private String soBbHaoDoi;
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
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private Long idBbTinhKho;
  private String soBbTinhKho;
  private BigDecimal tongSlNhap;
  private LocalDate ngayKtNhap;
  private BigDecimal tongSlXuat;
  private LocalDate ngayKtXuat;
  private BigDecimal slHaoThucTe;
  private String tiLeHaoThucTe;
  private BigDecimal slHaoVuotDm;
  private String tiLeHaoVuotDm;
  private BigDecimal slHaoThanhLy;
  private String tiLeHaoThanhLy;
  private BigDecimal slHaoDuoiDm;
  private String tiLeHaoDuoiDm;
  private BigDecimal dinhMucHaoHut;
  private String nguyenNhan;
  private String kienNghi;
  private String ghiChu;
  private Long idThuKho;
  private Long idKtvBaoQuan;
  private LocalDate ngayPduyetKtvBq;
  private Long idKeToan;
  private LocalDate ngayPduyetKt;
  private Long nguoiGduyetId;
  private LocalDate ngayGduyet;
  private Long nguoiPduyetId;
  private LocalDate ngayPduyet;
  private String donViTinh;
  private String trangThai;
  private String lyDoTuChoi;

  //@Transient
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
  private String tenThuKho;
  @Transient
  private String tenKtvBaoQuan;
  @Transient
  private String tenKeToan;
  @Transient
  private String tenLanhDaoChiCuc;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<FileDinhKem> fileDinhKems = new ArrayList<>();
  @Transient
  private List<XhDgBbHaoDoiDtl> listPhieuXuatKho = new ArrayList<>();
}