package com.tcdt.qlnvhang.request.object.vattu.hosokythuat;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhHoSoKyThuatReq {
    private Long id;
    private Long qdgnvnxId;
    private String bienBanGiaoMauId;
    private String maDvi;
    private String capDvi;
    private String soBienBan;
    private String maVatTuCha;
    private String maVatTu;
    private Long hopDongId;
    private LocalDate ngayHopDong;
    private String donViCungCap;
    private LocalDate ngayKiemTra;
    private String diaDiemKiemTra;
    private BigDecimal soLuongThucNhap;
    private LocalDate thoiGianNhap;
    private String ketLuan;
    private String vbtlCanBoSung;
    private String cbtlCanHoanThien;
    private LocalDate tgBsTruocNgay;
    private LocalDate tgHtTruocNgay;
    private String loaiVthh;

    private List<NhHoSoKyThuatCtReq> chiTiets = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKemReqs = new ArrayList<>();

    private List<FileDinhKemReq> fdkCanCus = new ArrayList<>();
}
