package com.tcdt.qlnvhang.response.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.response.SoBienBanPhieuRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NhBienBanChuanBiKhoRes extends SoBienBanPhieuRes {

    private Long id;

    private Long qdgnvnxId;
    private String soQuyetDinhNhap;

    private String soBienBan;

    private LocalDate ngayNghiemThu;
    private LocalDate ngayTao;

    private String thuTruongDonVi;

    private String keToanDonVi;

    private String kyThuatVien;

    private String thuKho;

    private String maVatTuCha; // Loai hang
    private String tenVatTuCha;
    private String maVatTu; // Chủng loại hàng
    private String tenVatTu;

    private String loaiHinhKho;

    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNganLo;
    private String tenNganLo;

    private String ptBaoQuan;

    private String thucNhap;

    private String htBaoQuan;

    private String ketLuan;

    private BigDecimal tongSo;
    private String tongSoBangChu;
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;

    private String lyDoTuChoi;
    private Long hopDongId;
    private String soHopDong;
    private List<NhBienBanChuanBiKhoCtRes> chiTiets = new ArrayList<>();
}

