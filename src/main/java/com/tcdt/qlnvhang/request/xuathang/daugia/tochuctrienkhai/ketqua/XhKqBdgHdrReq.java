package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhKqBdgHdrReq extends BaseRequest {

  private Long id;

  private Integer nam;

  private String maDvi;

  private String soQdKq;

  private String trichYeu;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayHluc;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayKy;

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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayPduyetTu;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayPduyetDen;

  // Transient
  @Transient
  private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

  @Transient
  private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

}
