package com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
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
public class NhBienBanChuanBiKhoReq extends SoBienBanPhieuReq {

    private Long id;

    private Long qdgnvnxId;

    private String soBienBan;

    private LocalDate ngayNghiemThu;

    private String thuTruongDonVi;

    private String keToanDonVi;

    private String kyThuatVien;

    private String thuKho;

    private String maVatTuCha; // Loai hang

    private String maVatTu; // Chủng loại hàng

    private String loaiHinhKho;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maNganLo;

    private String ptBaoQuan;

    private String thucNhap;

    private String htBaoQuan;

    private String ketLuan;

    private BigDecimal tongSo;

    private Long hopDongId;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;

    private List<NhBienBanChuanBiKhoCtReq> chiTiets = new ArrayList<>();
}

