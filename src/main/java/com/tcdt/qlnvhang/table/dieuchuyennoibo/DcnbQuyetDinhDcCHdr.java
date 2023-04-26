package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbQuyetDinhDcCHdr.TABLE_NAME)
@Getter
@Setter
public class DcnbQuyetDinhDcCHdr extends BaseEntity implements Serializable, Cloneable{

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "DCNB_QUYET_DINH_DC_C_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbQuyetDinhDcCHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = DcnbQuyetDinhDcCHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = DcnbQuyetDinhDcCHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String loaiDc;
  private String tenLoaiDc;
  private Integer nam;
  private String soQdinh;
  private LocalDate ngayKyQdinh;
  private LocalDate ngayDuyetTc;
  private Long nguoiDuyetTcId;
  private String trichYeu;
  private String maDvi;
  private String tenDvi;

  private String loaiQdinh;
  private String tenLoaiQdinh;
  private BigDecimal tongDuToanKp;
  private Long canCuQdTc;
  private String soCanCuQdTc;
  private Long dxuatId;
  private String soDxuat;
  private LocalDate ngayTrinhDuyetTc;


  @Access(value=AccessType.PROPERTY)
  private String trangThai;
  private String lyDoTuChoi;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();
  @Transient
  private List<FileDinhKem> quyetDinh = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "HDR_ID")
  private List<DcnbQuyetDinhDcCDtl> danhSachQuyetDinh = new ArrayList<>();

  public void setTrangThai(String trangThai) {
    this.trangThai = trangThai;
    this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
  }
}
