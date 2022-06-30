package com.tcdt.qlnvhang.response.vattu.hosokythuat;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhHoSoKyThuatRes {
    private Long id;
    private Long qdgnvnxId;
    private String soQuyetDinhNhap;
    private String bienBanGiaoMauId;
    private String soBbBanGiaoMau;
    private String maDvi;
    private String capDvi;
    private String tenDvi;
    private String soBienBan;
    private String maVatTuCha;
    private String tenVatTuCha;
    private String maVatTu;
    private String tenVatTu;
    private Long hopDongId;
    private String soHopDong;

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
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;

    private List<NhHoSoKyThuatCtRes> chiTiets = new ArrayList<>();

    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    private List<FileDinhKem> fdkCanCus = new ArrayList<>();
}
