package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtvtQuyetDinhPdHdrReq {
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soBbQd;
  private LocalDate ngayKy;
  private LocalDate ngayHluc;
  private Long idTongHop;
  private String maTongHop;
  private LocalDate ngayThop;
  private Long idDx;
  private String soDx;
  private LocalDate ngayDx;
  private BigDecimal tongSoLuongDx;
  private BigDecimal tongSoLuong;
  private Long thanhTien;
  private BigDecimal soLuongXuatCap;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private String loaiNhapXuat;
  private String kieuNhapXuat;
  private String mucDichXuat;
  private String trichYeu;
  private String trangThai;
  private String lyDoTuChoi;
  private String type;
  private Boolean xuatCap;
  private Boolean paXuatGaoChuyenXc;
  private Long qdPaXuatCapId;
  private String qdPaXuatCap;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String donViTinh;
  private LocalDate ngayKetThuc;
  private Long idQdGiaoNv;
  private String soQdGiaoNv;
  private LocalDate ngayTapKet;
  private LocalDate ngayGiaoHang;

  private List<FileDinhKemJoinTable> fileDinhKem;

  private List<FileDinhKemJoinTable> canCu = new ArrayList<>();

  private List<XhCtvtQuyetDinhPdDtl> quyetDinhPdDtl = new ArrayList<>();
}

