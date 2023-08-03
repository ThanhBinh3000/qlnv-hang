package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTDtl;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DcnbBangKeXuatVTReq extends BaseRequest {
    private Integer nam;
    private String maDvi;
    private String loaiDc;
    private String loaiQdinh;
    private String typeQd;
    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();

    private Long id;
    private String loaiVthh;
    private String cloaiVthh;
    private String soBangKe;
    private LocalDate ngayNhap;
    private Long qhnsId;
    private String maQhns;
    private Long qDinhDccId;
    private String soQdinhDcc;
    private LocalDate ngayKyQdinhDcc;
    private String soHopDong;
    private Long phieuXuatKhoId;
    private String soPhieuXuatKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String diaDaDiemKho;
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

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;
    private String type;
    private List<DcnbBangKeXuatVTDtl> dcnbBangKeXuatVTDtl = new ArrayList<>();

    private LocalDate tuNgayThoiHan;
    private LocalDate denNgayThoiHan;

    private LocalDate tuNgayXuatKho;
    private LocalDate denNgayXuatKho;
}
