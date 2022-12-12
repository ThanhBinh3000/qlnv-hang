package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = XhTcTtinBdgHdr.TABLE_NAME)
public class XhTcTtinBdgHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TC_TTIN_BDG_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TC_TTIN_BDG_HDR_SEQ")
  @SequenceGenerator(sequenceName = "XH_TC_TTIN_BDG_HDR_SEQ", allocationSize = 1, name = "XH_TC_TTIN_BDG_HDR_SEQ")
  private Long id;
  private int nam;
  private String maDvi;
  private String loaiVthh;
  private String cloaiVthh;
  private Long idQdPdKh;
  private String soQdPdKh;
  private Long idQdDcKh;
  private String soQdDcKh;
  private Long idQdPdKq;
  private String soQdPdKq;
  private Long idKhDx;
  private String soKhDx;
  private LocalDate ngayQdPdKqBdg;
  private int thoiHanGiaoNhan;
  private int thoiHanThanhToan;
  private String phuongThucThanhToan;
  private String phuongThucGiaoNhan;
  private String trangThai;
  private String maDviThucHien;
  private Long tongTienGiaKhoiDiem;
  private Long tongTienDatTruoc;
  private Long tongTienDatTruocDuocDuyet;
  private Long tongSoLuong;
  private int phanTramDatTruoc;
  private LocalDate thoiGianToChucTu;
  private LocalDate thoiGianToChucDen;
  @Transient
  private String tenDvi;
  @Transient
  private String tenDviThucHien;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private int soDviTsan;
  @Transient
  private int soDviTsanThanhCong;
  @Transient
  private int soDviTsanKhongThanh;
  @Transient
  private List<XhTcTtinBdgDtl> detail = new ArrayList<>();
  @Transient
  List<XhTcTtinBdgTaiSan> listTaiSanQd = new ArrayList<>();


}
