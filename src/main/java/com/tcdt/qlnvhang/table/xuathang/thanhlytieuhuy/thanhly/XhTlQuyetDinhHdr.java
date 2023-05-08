package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

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
@Table(name = XhTlQuyetDinhHdr.TABLE_NAME)
@Data
public class XhTlQuyetDinhHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TL_QUYET_DINH_TL_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlQuyetDinhHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhTlQuyetDinhHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhTlQuyetDinhHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;
  private Long idHoSo;
  private String soHoSo;
  private Long idKq;
  private String soKq;
  private LocalDate thoiGianTlTu;
  private LocalDate thoiGianTlDen;
  private String trichYeu;
  private String trangThai;
  private BigDecimal tongSoLuongTl;
  private BigDecimal tongSoLuongCon;
  private BigDecimal tongThanhTien;

  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private String lyDoTuChoi;

  @Transient
  private String tenDvi;
  @Transient
  private String tenTrangThai;

  @Transient
  private List<FileDinhKem> fileDinhKem;
  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();

  @OneToMany(mappedBy = "quyetDinhHdr", cascade = CascadeType.ALL)
  private List<XhTlQuyetDinhDtl> quyetDinhPdDtl = new ArrayList<>();
}
