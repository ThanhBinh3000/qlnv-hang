package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeXuatVatTuDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
public class ScBangKeXuatVatTuReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private Long qhnsId;
    private String maQhns;
    private String soBangKe;
    private LocalDate ngayNhap;
    private String soQdGiaoNv;
    private LocalDate ngayKyQdGiaoNv;
    private Long phieuXuatKhoId;
    private String soPhieuXuatKho;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String diaDiemKho;
    private String maLanhDaoChiCuc;
    private String tenLanhDaoChiCuc;
    private Long thuKhoId;
    private String tenThuKho;
    private String tenNguoiGiaoHang;
    private String cccd;
    private String donViNguoiGiaoHang;
    private String diaChiDonViNguoiGiaoHang;
    private LocalDate thoiHanGiaoNhan;
    private String loaiHang;
    private String chungLoaiHang;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    List<ScBangKeXuatVatTuDtl> children = new ArrayList<>();

}
