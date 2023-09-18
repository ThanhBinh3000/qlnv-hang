package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

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
@Table(name = XhCtvtBbTinhKhoHdr.TABLE_NAME)
@Data
public class XhCtvtBbTinhKhoHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_BB_TINH_KHO_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtBbTinhKhoHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtBbTinhKhoHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtBbTinhKhoHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soBbTinhKho;
  private LocalDate ngayTaoBb;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private LocalDate ngayBatDauXuat;
  private LocalDate ngayKetThucXuat;
  private BigDecimal tongSlNhap;
  private BigDecimal tongSlXuat;
  private BigDecimal slConLai;
  private BigDecimal slThucTeCon;
  private BigDecimal slThua;
  private BigDecimal slThieu;
  private String nguyenNhan;
  private String kienNghi;
  private String ghiChu;
  private String thuKho;
  private String ktvBaoQuan;
  private String keToan;
  private String ldChiCuc;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  @Transient
  private String tenDvi;
  @Transient
  private String tenDviCha;
  @Transient
  private String diaChiDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenDiemKho;
  @Transient
  private String tenNhaKho;
  @Transient
  private String tenNganKho;
  @Transient
  private String tenLoKho;
  @Transient
  private List<FileDinhKem> fileDinhKems =new ArrayList<>();
  @OneToMany(mappedBy = "xhCtvtBbTinhKhoHdr", fetch = FetchType.LAZY)
  private List<XhCtvtBbTinhKhoDtl> listPhieuXuatKho= new ArrayList<>();
}
