package com.tcdt.qlnvhang.table.xuathang.hosokythuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhHoSoKyThuatDtl.TABLE_NAME)
@Data
public class XhHoSoKyThuatDtl extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_HO_SO_KY_THUAT_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhHoSoKyThuatDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhHoSoKyThuatDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhHoSoKyThuatDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private String soBienBan;
  private LocalDate ngayLapBb;
  private String soHskt;
  private String maDviNhapHskt;
  private LocalDate ngayTaoHskt;
  private Long idBbLayMau;
  private String soBbLayMau;
  private String soQdGiaoNvNh;
  private Long idQdGiaoNvNh;
  private String dviCungCap;
  private LocalDate tgianKtra;
  private String kquaKtra;
  private String diaDiemKtra;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private Long soLuongNhap;
  private LocalDate tgianNhap;
  private String ppLayMau;
  private String kyMaHieu;
  private String soSerial;
  private String noiDung;
  private String ketLuan;
  private String trangThai;
  //  BB_KTRA_NGOAI_QUAN = "BBKTNQ",
  //  BB_KTRA_VAN_HANH = "BBKTVH",
  //  BB_KTRA_HOSO_KYTHUAT = "BBKTHSKT",
  private String loaiBb;
  private String thoiDiemLap;
  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();
  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();
  @Transient
  private List<FileDinhKem> vanBanBsung = new ArrayList<>();
  private LocalDate tgianBsung;

  @OneToMany(mappedBy = "xhHoSoKyThuatDtl", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<XhHoSoKyThuatRow> xhHoSoKyThuatRow = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhHoSoKyThuatHdr xhHoSoKyThuatHdr;
}
