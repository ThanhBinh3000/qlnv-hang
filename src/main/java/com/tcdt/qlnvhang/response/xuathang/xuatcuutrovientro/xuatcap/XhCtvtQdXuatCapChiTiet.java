package com.tcdt.qlnvhang.response.xuathang.xuatcuutrovientro.xuatcap;

import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapDtlReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class XhCtvtQdXuatCapChiTiet {
  private Long id;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;
  private LocalDate ngayHluc;
  private String trichYeu;
  private String trangThai;
  private String loaiVthh;
  private String tenLoaiVthh;
  private String tenCloaiVthh;
  private String tenTrangThai;
  private String loaiNhapXuat;
  private String kieuNhapXuat;
  private LocalDate thoiHanXuat;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private BigDecimal tongSoLuongThoc;
  private BigDecimal tongSoLuongGao;
  private BigDecimal thanhTien;
  private List<XhCtvtQdXuatCapDtlReq> deXuatPhuongAn;
  private List<FileDinhKem> fileDinhKem;
  private List<FileDinhKem> canCu;
  private List<XhCtvtQuyetDinhPdDtl> quyetDinhPdDtl;
  private Boolean isChonPhuongAn;
  private Long qdPaXuatCapId;
}
