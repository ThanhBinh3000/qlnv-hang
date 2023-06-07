package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhThTongHopHdr.TABLE_NAME)
@Getter
@Setter
public class XhThTongHopHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TH_TONG_HOP_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThTongHopHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhThTongHopHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhThTongHopHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDanhSach;
  private String tenDanhSach;
  private LocalDate thoiGianTlTu;
  private LocalDate thoiGianTlDen;
  private String trangThai;
  private String trangThaiTl;
  private Long idHoSo;
  private String soHoSo;
  private Long idQdPd;
  private String soQdPd;
  private LocalDate ngayKyQd;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private BigDecimal tongSlHienTai;
  private BigDecimal tongSlDeXuat;
  private BigDecimal tongSlDaDuyet;

  @Transient
  private String tenTrangThai;
  @Transient
  private String tenTrangThaiTl;

  @Transient
  private String tenDvi;

  @Transient
  private String maDvql;
  @Transient
  private String tenDvql;

  @OneToMany(mappedBy = "tongHopHdr", cascade = CascadeType.ALL)
  private List<XhThTongHopDtl> tongHopDtl = new ArrayList<>();
}
