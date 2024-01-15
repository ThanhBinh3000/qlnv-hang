package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXuatCapPhieuKtClHdrReq {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soPhieuKtCl;
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
  private BigDecimal soLuongXuat;
  private BigDecimal soLuongNhan;
  private String ketQua;
  private String ketLuan;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  private String soBbTinhKho;
  private String chungLoaiGao;

  private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
  private List<XhXuatCapPhieuKtClDtlReq> ketQuaPhanTich= new ArrayList<>();

}
