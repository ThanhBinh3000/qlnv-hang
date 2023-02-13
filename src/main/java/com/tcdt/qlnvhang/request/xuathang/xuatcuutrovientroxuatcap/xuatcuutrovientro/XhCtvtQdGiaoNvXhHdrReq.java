package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtvtQdGiaoNvXhHdrReq {
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;
  private Long idQdPd;
  private String soQdPd;
  private String loaiVthh;
  private String cloaiVthh;
  private BigDecimal soLuong;
  private BigDecimal dviTinh;
  private LocalDate thoiGianGiaoNhan;
  private String trichYeu;
  private String trangThai;
  private String lyDoTuChoi;
  private String trangThaiXh;
  private String soBbHaoDoi;
  private String soBbTinhKho;
  private BigDecimal tongSoLuong;
  private BigDecimal thanhTien;
  private String type;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;

  private List<FileDinhKemReq> canCu = new ArrayList<>();
  private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

  private List<XhCtvtQdGiaoNvXhDtlReq> noiDungCuuTro = new ArrayList<>();
}
