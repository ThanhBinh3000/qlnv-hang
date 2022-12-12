

package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

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
@Table(name = XhTcTtinBdgThongBao.TABLE_NAME)
public class XhTcTtinBdgThongBao extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TC_TTIN_BDG_THONG_BAO";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TC_TTIN_BDG_THONG_BAO_SEQ")
  @SequenceGenerator(sequenceName = "XH_TC_TTIN_BDG_THONG_BAO_SEQ", allocationSize = 1, name = "XH_TC_TTIN_BDG_THONG_BAO_SEQ")
  private Long id;
  private Long idTtin;
  private String maDvi;
  private String soTb;
  private String trichYeu;
  private String toChucTen;
  private String toChucDiaChi;
  private String toChucSdt;
  private String toChucStk;
  private String soHopDong;
  private LocalDate ngayKyHopDong;
  private String hinhThucToChuc;
  private LocalDate thoiHanDkTu;
  private LocalDate thoiHanDkDen;
  private String ghiChuThoiGianDk;
  private String ghiChuThoiGianXem;
  private String diaDiemNopHoSo;
  private String diaDiemXemTaiSan;
  private Long dieuKienDk;
  private Long buocGia;
  private LocalDate thoiHanXemTaiSanTu;
  private LocalDate thoiHanXemTaiSanDen;
  private LocalDate thoiHanNopTienTu;
  private LocalDate thoiHanNopTienDen;
  private String phuongThucThanhToan;
  private String huongThuTen;
  private String huongThuStk;
  private String huongThuNganHang;
  private String huongThuChiNhanh;
  private LocalDate thoiGianToChucTu;
  private LocalDate thoiGianToChucDen;
  private String diaDiemToChuc;
  private String hinhThucDauGia;
  private String phuongThucDauGia;
  private String ghiChu;

  @Transient
  private String tenDvi;
  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();
}
