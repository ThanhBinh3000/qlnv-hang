package com.tcdt.qlnvhang.table.xuathang.thanhly;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhTlDanhSachHdr.TABLE_NAME)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class XhTlDanhSachHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TL_DANH_SACH_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlDanhSachHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhTlDanhSachHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhTlDanhSachHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDanhSach;
  private LocalDate ngayThop;
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

  private BigDecimal tongSlCtVt;
  private BigDecimal tongSlXuatCap;
  private BigDecimal tongSlDeXuat;


  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenDvi;

  @OneToMany(mappedBy = "xhTlDanhSachHdr", cascade = CascadeType.ALL)
//    @Transient
  private List<XhTlDanhSachDtl> danhSachDtls = new ArrayList<>();

}
