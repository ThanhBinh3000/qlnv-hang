package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhThHoSoHdr.TABLE_NAME)
@Getter
@Setter
public class XhThHoSoHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TH_HO_SO_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThHoSoHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhThHoSoHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhThHoSoHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String soHoSo;
  private LocalDate ngayTaoHs;
  private Long idDanhSach;
  private String maDanhSach;
  private Long idQd;
  private String soQd;
  private Long idTb;
  private String soTb;
  private LocalDate thoiGianThTu;
  private LocalDate thoiGianThDen;
  private LocalDate thoiGianPdTu;
  private LocalDate thoiGianPdDen;
  private String trangThai;
  private String trangThaiTc;
  private String ketQua;
  private String trichYeu;
  private LocalDate ngayKyQd;
  private LocalDate ngayDuyetLdc;
  private Long idLdc;
  private LocalDate ngayDuyetLdv;
  private Long idLdv;
  private LocalDate ngayDuyetLdtc;
  private Long idLdtc;
  private String lyDoTuChoi;
  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();;
  @Transient
  private List<FileDinhKem> fileCanCu = new ArrayList<>();
  @Transient
  private List<XhThHoSoDtl> children = new ArrayList<>();


  @Transient
  private String tenTrangThai;

  @Transient
  private String tenTrangThaiTc;

  @Transient
  private String tenDvi;

  public String getTenTrangThai(){
    return TrangThaiAllEnum.getLabelById(getTrangThai());
  }

  public String getTenTrangThaiTc(){
    return TrangThaiAllEnum.getLabelById(getTrangThaiTc());
  }

}
