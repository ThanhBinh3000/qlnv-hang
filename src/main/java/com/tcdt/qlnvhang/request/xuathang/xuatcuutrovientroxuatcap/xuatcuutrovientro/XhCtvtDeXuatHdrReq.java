package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatPa;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtvtDeXuatHdrReq {

    private Long id;
    private int nam;
    private String maDvi;
    private String loaiNhapXuat;
    private String kieuNhapXuat;
    private String soDx;
    private String trichYeu;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenVthh;
    private BigDecimal tonKho;
    private LocalDate ngayDx;
    private LocalDate ngayKetThuc;
    private String noiDungDx;
    private String trangThai;
    private Long idThop;
    private String maTongHop;
    private Long idQdPd;
    private String soQdPd;
    private LocalDate ngayKyQd;
    private BigDecimal tongSoLuong;
    private BigDecimal soLuongXuatCap;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private String type;
    private BigDecimal thanhTien;

    private List<FileDinhKemReq> canCu = new ArrayList<>();

    private List<XhCtvtDeXuatPa> deXuatPhuongAn = new ArrayList<>();
}
