package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXuatCapPhieuKnClHdrReq {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soPhieuKnCl;
  private LocalDate ngayLapPhieu;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate thoiHanXuatCtVt;
  private String nguoiKn;
  private String truongPhong;
  private String thuKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private String hinhThucBq;
  private String noiDung;
  private String ketLuan;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  private String soBbTinhKho;

  @Transient
  private String tenDvi;
  @Transient
  private String diaChiDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenChiCuc;
  @Transient
  private String tenDiemKho;
  @Transient
  private String tenNhaKho;
  @Transient
  private String tenNganKho;
  @Transient
  private String tenLoKho;
  @Transient
  private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
  @Transient
  private List<XhXuatCapPhieuKnClDtlReq> ketQuaPhanTich= new ArrayList<>();

}
