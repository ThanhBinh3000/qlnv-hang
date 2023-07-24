package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = XhXkVtBhQdGiaonvXhHdr.TABLE_NAME)
public class XhXkVtBhQdGiaonvXhHdr extends BaseEntity implements Serializable {

  public static final String TABLE_NAME = "XH_XK_VT_BH_QD_GNV_XH_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBhQdGiaonvXhHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXkVtBhQdGiaonvXhHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhXkVtBhQdGiaonvXhHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String soQuyetDinh;
  private String loai;
  private String trichYeu;
  private LocalDate ngayKy;
  private Integer soLanLm;
  private LocalDate thoiHanXuatHang;
  private String soCanCu;
  private Long idCanCu;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String trangThaiXh;
  private String loaiCanCu;
  private String soBaoCaoKdm;
  private Long idBaoCaoKdm;


  @Transient
  private String tenTrangThai;
  @Transient
  private String tenTrangThaiXh;
  @Transient
  private String tenDvi;
  @Transient
  private String tenLoai;

  @Transient
  private List<FileDinhKem> canCu;
  @Transient
  private List<FileDinhKem> fileDinhKems;
  @OneToMany(mappedBy = "qdGiaonvXhHdr", cascade = CascadeType.ALL)
  private List<XhXkVtBhQdGiaonvXhDtl> qdGiaonvXhDtl = new ArrayList<>();


}
