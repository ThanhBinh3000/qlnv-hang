package com.tcdt.qlnvhang.response.vattu.bangke;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhBangKeVtRes {
    private Long id;
    private Long qdgnvnxId;
    private String soQuyetDinhNhap;
    private String soBangKe;
    private Long phieuNhapKhoId;
    private String soPhieuNhapKho;
    private String maVatTuCha;
    private String tenVatTuCha;
    private String maVatTu;
    private String tenVatTu;
    private String donViTinh;
    private String diaChiNguoiGiao;
    private Long hopDongId;
    private String soHopDong;
    private LocalDate ngayNhap;
    private LocalDate ngayTaoBangKe;
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;
    private String lyDoTuChoi;

    private String tenDiemKho;
    private String maDiemKho;
    private String tenNhaKho;
    private String maNhaKho;
    private String tenNganKho;
    private String maNganKho;
    private String tenNganLo;
    private String maNganLo;

    private List<NhBangKeVtCtRes> chiTiets = new ArrayList<>();
}
