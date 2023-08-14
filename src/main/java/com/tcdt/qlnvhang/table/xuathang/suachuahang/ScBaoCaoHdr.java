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
import java.util.Date;
import java.util.List;

@Entity
@Table(name = ScBaoCaoHdr.TABLE_NAME)
@Getter
@Setter
public class ScBaoCaoHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "SC_BAO_CAO_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScTongHopHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = ScTongHopHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScTongHopHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDviNhan;
  private String soBaoCao;
  private LocalDate ngayBaoCao;
  private String tenBaoCao;
  private String soQdXh;
  private Long idQdXh;
  private LocalDate ngayQdXh;
  private String soQdTc;
  private LocalDate ngayQdTc;
  private String noiDung;
  private String trangThai;
  private String lyDoTuChoi;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenDvi;
  @Transient
  private String tenDviNhan;
  @Transient
  private List<ScBaoCaoDtl> children = new ArrayList<>();
  @Transient
  private List<FileDinhKem> fileDinhKems = new ArrayList<>();

  public String getTenTrangThai(){
    return TrangThaiAllEnum.getLabelById(getTrangThai());
  }

}
