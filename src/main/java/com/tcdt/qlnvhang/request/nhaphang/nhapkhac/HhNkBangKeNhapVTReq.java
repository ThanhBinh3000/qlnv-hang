package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeNhapVTDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HhNkBangKeNhapVTReq extends BaseRequest {
    private Long id;
    private String loaiDc;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private Integer nam;
    private String soBangKe;
    private LocalDate ngayNhap;
    private String maDvi;
    private String tenDvi;
    private Long qhnsId;
    private String maQhns;
    private Long qdPdNkId;
    private String soQdPdNk;
    private LocalDate ngayQdPdNk;
    private String soHopDong;
    private Long phieuNhapKhoId;
    private String soPhieuNhapKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private String maLanhDaoChiCuc;
    private String tenLanhDaoChiCuc;
    private Long thuKhoId;
    private String tenThuKho;
    private Long phuTrachId;
    private String tenPhuTrach;
    private String tenNguoiGiaoHang;
    private String cccd;
    private String donViNguoiGiaoHang;
    private String diaChiDonViNguoiGiaoHang;
    private LocalDate thoiHanGiaoNhan;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiGDuyet;
    private LocalDate ngayGDuyet;
    private Long nguoiPDuyetTvqt;
    private LocalDate ngayPDuyetTvqt;
    private Long nguoiPDuyet;
    private LocalDate ngayPDuyet;
    private String type;
    private List<HhNkBangKeNhapVTDtl> hhNkBangKeNhapVTDtl = new ArrayList<>();

    private LocalDate tuNgayThoiHan;
    private LocalDate denNgayThoiHan;

    private LocalDate tuNgayNhapKho;
    private LocalDate denNgayNhapKho;
}
