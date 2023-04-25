package com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
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
@Table(name = XhKqBdgHdr.TABLE_NAME)
public class XhKqBdgHdr extends TrangThaiBaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_KQ_BDG_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_KQ_BDG_HDR_SEQ")
  @SequenceGenerator(sequenceName = "XH_KQ_BDG_HDR_SEQ", allocationSize = 1, name = "XH_KQ_BDG_HDR_SEQ")
  private Long id;

  private Integer nam;

  private String maDvi;
  @Transient
  private String tenDvi;

  private String soQdKq;

  private String trichYeu;


  private LocalDate ngayHluc;

  private LocalDate ngayKy;

  private String loaiHinhNx;

  private String kieuNx;

  private String loaiVthh;
  @Transient
  private String tenLoaiVthh;

  private String cloaiVthh;
  @Transient
  private String tenCloaiVthh;

  private String maThongBao;

  private String soBienBan;

  private String pthucGnhan;

  private Integer tgianGnhan;

  private String tgianGnhanGhiChu;

  private String ghiChu;

  private String soQdPd;

  private String hinhThucDauGia;

  private String pthucDauGia;

  private String soTbKhongThanh;

  private Long tongDviTs;

  private Long soDvtsDgTc;

  private Long slHdDaKy;

  private LocalDate thoiHanTt;

  private String toChucCaNhanDg;

  private Long tongSlXuat;

  private Long thanhTien;

  private String trangThaiHd;
  @Transient
  private String tenTrangThaiHd;

  private String trangThaiXh;
  @Transient
  private String tenTrangThaiXh;

  @Transient
  private List<FileDinhKem> fileDinhKems = new ArrayList<>();

  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();



  @Transient
  private List<XhHopDongHdr> listHopDong;

  public String getTenTrangThaiHd() {
    return NhapXuatHangTrangThaiEnum.getTenById(trangThaiHd);
  }

  public String getTenTrangThaiXh() {
    return NhapXuatHangTrangThaiEnum.getTenById(trangThaiXh);
  }


}
