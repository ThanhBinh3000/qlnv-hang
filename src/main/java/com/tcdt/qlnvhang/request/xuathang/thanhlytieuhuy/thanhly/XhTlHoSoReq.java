package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlHoSoReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String soHoSo;
    private String soTtrinhVu;
    private Long idDanhSach;
    private String maDanhSach;
    private Long idQd;
    private String soQd;
    private Long idTb;
    private String soTb;
    private LocalDate ngayTaoHs;
    private LocalDate thoiGianTlTu;
    private LocalDate thoiGianTlDen;
    private String trangThai;
    private LocalDate ngayDuyetLan1;
    private LocalDate ngayDuyetLan2;
    private LocalDate ngayDuyetLan3;
    private String trichYeu;
    private String dvql;
    private List<FileDinhKemReq> fileDinhKemReq = new ArrayList<>();
    private List<FileDinhKemReq> fileCanCuReq = new ArrayList<>();
    private List<XhTlHoSoDtlReq> children = new ArrayList<>();

    private Long idTongHop;
    private Long idDsHdr;
    private String maTongHop;
    private String maDiaDiem;
    private String loaiVthh;
    private String cloaiVthh;
    private String donViTinh;
    private BigDecimal slHienTai;
    private BigDecimal slDeXuat;
    private BigDecimal slDaDuyet;
    private BigDecimal thanhTien;
    private LocalDate ngayNhapKho;
    private LocalDate ngayDeXuat;
    private LocalDate ngayTongHop;
    private String lyDo;
    private String type;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private String ketQuaDanhGia;
    private String ketQua;
    private LocalDate ngayTuCuc;
    private LocalDate ngayDenCuc;
    private LocalDate ngayTuTc;
    private LocalDate ngayDenTc;

}
