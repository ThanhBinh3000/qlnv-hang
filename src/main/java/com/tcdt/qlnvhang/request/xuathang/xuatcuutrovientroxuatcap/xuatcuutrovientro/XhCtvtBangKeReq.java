package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBangKeDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtvtBangKeReq extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhns;
  private String soBangKe;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private Long idPhieuXuatKho;
  private String soPhieuXuatKho;
  private LocalDate ngayXuat;
  private String diaDiemKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String thuKho;
  private String nlqHoTen;
  private String nlqCmnd;
  private String nlqDonVi;
  private String nlqDiaChi;
  private LocalDate thoiGianGiaoNhan;
  private BigDecimal tongTrongLuong;
  private BigDecimal tongTrongLuongBaoBi;
  private BigDecimal tongTrongLuongHang;
  private String tongTrongLuongHangBc;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String trangThai;
  private String type;
  private LocalDate ngayLapBangKe;
  private String nguoiGiamSat;
  private String idPhieuKnCl;
  private String soPhieuKnCl;
  private List<FileDinhKemJoinTable> fileDinhKem;
  private List<XhCtvtBangKeDtl> bangKeDtl= new ArrayList<>();
}
