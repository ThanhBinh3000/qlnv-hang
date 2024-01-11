package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkKhXuatHangRequest extends BaseRequest {
    private Long id;
    Integer namKeHoach;
    String maDvi;
    String diaChi;
    String loaiHinhNhapXuat;
    String kieuNhapXuat;
    String soToTrinh;
    String trichYeu;
    LocalDate ngayKeHoach;
    LocalDate ngayDuyetKeHoach;
    LocalDate ngayDuyetBtc;
    LocalDate thoiGianDuKienXuatTu;
    LocalDate thoiGianDuKienXuatDen;
    String soQdBtc;
    LocalDate ngayDxXuatHangTu;
    LocalDate ngayDxXuatHangDen;
    LocalDate ngayTrinhDuyetBtc;
    String moTa;
    String maTongHopDs;
    Long idTongHopDs;
    String trangThai;
    Integer capDvi;
    List<XhXkKhXuatHangDtl> xhXkKhXuatHangDtl = new ArrayList<>();
    List<FileDinhKem> fileDinhKems;
    //Entity Tổng hợp kế hoạch xuất hàng
    String loai;
    LocalDate thoiGianTh;
    String noiDungTh;
    String ghiChu;
    Long idCanCu;
    List<Long> listIdKeHoachs;
    //search params
    LocalDate ngayKeHoachTu;
    LocalDate ngayKeHoachDen;
    LocalDate ngayDuyetKeHoachTu;
    LocalDate ngayDuyetKeHoachDen;
    LocalDate ngayDuyetBtcTu;
    LocalDate ngayDuyetBtcDen;
    LocalDate thoiGianThTu;
    LocalDate thoiGianThDen;
    String dvql;
}
