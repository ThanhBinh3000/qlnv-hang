package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdGnvCuuTroHdr.TABLE_NAME)
@Data
public class XhQdGnvCuuTroHdr extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_GNV_CUU_TRO_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdGnvCuuTroHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhQdGnvCuuTroHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhQdGnvCuuTroHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String loaiNhapXuat;
  private String soDxuat;
  private String maDvi;

  private int nam;
  private String soQd;
  private LocalDate ngayKy;
  private Long idQdPd;
  private String soQdPd;
  private String soBbHaoDoi;
  private String soBbTinhKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private Long tongSoLuong;
  private LocalDate thoiGianGiaoNhan;
  private String trichYeu;
  private String trangThai;
  private String trangThaiXh;

  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();
  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();

  private String lyDoTuChoi;
  @Transient
  private String tenDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<XhQdGnvCuuTroDtl> noiDungCuuTro = new ArrayList<>();

}
