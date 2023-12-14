package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdGiaoNvXhDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class XhCtvtQuyetDinhGnvHdrReq {
  LocalDateTime ngayTao;
  Long nguoiTaoId;
  LocalDateTime ngaySua;
  Long nguoiSuaId;
  Long id;
  String maDvi;
  Integer nam;
  String soBbQd;
  LocalDate ngayKy;
  Long idQdPd;
  String soQdPd;
  String loaiVthh;
  String cloaiVthh;
  String tenVthh;
  BigDecimal soLuong;
  BigDecimal soLuongDx;
  BigDecimal soLuongGiao;
  LocalDate thoiGianGiaoNhan;
  String trichYeu;
  String trangThai;
  String lyDoTuChoi;
  String trangThaiXh;
  String soBbHaoDoi;
  String soBbTinhKho;
  BigDecimal tongSoLuong;
  BigDecimal thanhTien;
  String type;
  LocalDate ngayGduyet;
  Long nguoiGduyetId;
  LocalDate ngayPduyet;
  Long nguoiPduyetId;
  String loaiNhapXuat;
  String kieuNhapXuat;
  String mucDichXuat;
  String tenDvi;
  String tenLoaiVthh;
  String tenCloaiVthh;
  String tenTrangThai;
  String tenTrangThaiXh;
  LocalDate ngayTapKet;
  LocalDate ngayGiaoHang;
  List<XhCtvtQuyetDinhGnvDtl> dataDtl;
  List<FileDinhKemJoinTable> fileDinhKem;
  List<FileDinhKemJoinTable> canCu;
}
