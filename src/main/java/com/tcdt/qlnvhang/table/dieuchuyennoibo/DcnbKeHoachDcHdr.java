package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbKeHoachDcHdr.TABLE_NAME)
@Getter
@Setter
public class DcnbKeHoachDcHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "DCNB_KE_HOACH_DC_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbKeHoachDcHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = DcnbKeHoachDcHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = DcnbKeHoachDcHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String loaiDc;
  private Integer nam;
  private String soDxuat;
  private LocalDate ngayLapKh;
  private LocalDate ngayDuyetLdc;
  private String trichYeu;
  private String lyDoDc;
  private String maDvi;
  private String maCucNhan;
  private String trachNhiemDviTh;
  private String trangThai;
  private String lyDoTuChoi;
  private String type;
  private Long idThop;
  private String maThop;
  private Long idQdDc;
  private String soQdDc;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  @Transient
  private String tenDvi;
  @Transient
  private String tenCucDxuat;
  @Transient
  private String tenCucNhan;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();

  @OneToMany(mappedBy = "dcnbKeHoachDcHdr",cascade = CascadeType.ALL)
  private List<DcnbKeHoachDcDtl> dcNbKeHoachDcDtl = new ArrayList<>();

  public void maDvi(String maDvi) {
    this.maDvi = maDvi;
    setTenCucDxuat(maDvi.length() >= 6 ? maDvi.substring(0, 6) : "");
  }
}
