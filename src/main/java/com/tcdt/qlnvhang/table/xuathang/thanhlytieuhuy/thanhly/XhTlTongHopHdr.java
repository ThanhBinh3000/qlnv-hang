package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhTlTongHopHdr.TABLE_NAME)
@Data
public class XhTlTongHopHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TL_TONG_HOP_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlTongHopHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhTlTongHopHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhTlTongHopHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDanhSach;
  private LocalDate thoiGianTlTu;
  private LocalDate thoiGianTlDen;
  private String trangThai;
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
  private String tenDvi;

  @OneToMany(mappedBy = "danhSachHdr", cascade = CascadeType.ALL)
  private List<XhTlTongHopDtl> tongHopDtl = new ArrayList<>();

}
