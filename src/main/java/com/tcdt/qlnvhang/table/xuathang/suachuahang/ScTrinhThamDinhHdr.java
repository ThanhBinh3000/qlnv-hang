package com.tcdt.qlnvhang.table.xuathang.suachuahang;

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
@Table(name = ScTrinhThamDinhHdr.TABLE_NAME)
@Getter
@Setter
public class ScTrinhThamDinhHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "SC_TRINH_THAM_DINH_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScTrinhThamDinhHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = ScTrinhThamDinhHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScTrinhThamDinhHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String soTtr;
  private Long idThHdr;
  private String maThHdr;
  private LocalDate ngayDuyetLdc;
  private LocalDate thoiHanNhapDk;
  private LocalDate thoiHanNhap;
  private LocalDate thoiHanXuatDk;
  private LocalDate thoiHanXuat;
  private String soQdSc;
  private Long idQdSc;
  private String trichYeu;
  private String ysKien;
  private String lyDoTuChoi;
  private String trangThai;
  private String ketQua;
  private LocalDate ngayDuyetLdv;
  private LocalDate ngayDuyetLdtc;
  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();;
  @Transient
  private List<FileDinhKem> fileCanCu = new ArrayList<>();
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenDvi;
  @Transient
  private List<ScTrinhThamDinhDtl> children = new ArrayList<>();

  public String getTenTrangThai(){
    return TrangThaiAllEnum.getLabelById(getTrangThai());
  }


}
