package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhKqBdgHdrReq extends BaseRequest {

  private Long id;

  private Integer nam;

  private Long idPdKhDtl;

  private Long idThongTin;

  private String maDvi;

  private String soQdKq;

  private String trichYeu;

  private LocalDate ngayHluc;

  private LocalDate ngayKy;

  @Transient
  private LocalDate ngayKyTu;

  @Transient
  private LocalDate ngayKyDen;

  private String loaiHinhNx;

  private String kieuNx;

  private String loaiVthh;

  private String cloaiVthh;

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

  private String trangThaiHd;

  private String trangThaiXh;

  private Long tongDvts;

  private Long soDvtsDgTc;

  private Long slHdDaKy;

  private LocalDate thoiHanTt;

  private String toChucCaNhanDg;

  private Long tongSlXuat;

  private Long thanhTien;

  // Transient
  @Transient
  private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

  @Transient
  private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

}
