package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DcnbPhieuXuatKhoHdrDTO {
    private Long id;
    private Long qDinhDccId;
    private String soQdinh;
    private LocalDate ngayKyQdinh;
    private Integer namKh;
    private LocalDate thoiHanDieuChuyen;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private Boolean thayDoiThuKho;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKho;
    private Long phieuKiemNghiemId;
    private String soPhieuKiemNghiemCl;
    private LocalDate ngayGiamDinh;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String ktvBaoQuan;
    private String donViTinh;
    private BigDecimal slDienChuyen;
    private BigDecimal duToanKinhPhiDc;
    private Long bKCanHangId;
    private String soBKCanHang;
    private Long bangKeVtId;
    private String soBangKeVt;
    private String trangThai;
    private String tenTrangThai;
    private Long keHoachDcDtlId;
    public DcnbPhieuXuatKhoHdrDTO(Long id, Long qDinhDccId, String soQdinh, LocalDate ngayKyQdinh, Integer namKh, LocalDate thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, Boolean thayDoiThuKho, String soPhieuXuatKho, LocalDate ngayXuatKho, Long phieuKiemNghiemId, String soPhieuKiemNghiemCl, LocalDate ngayGiamDinh, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String ktvBaoQuan, String donViTinh, BigDecimal slDienChuyen, BigDecimal duToanKinhPhiDc, Long bKCanHangId, String soBKCanHang, Long bangKeVtId, String soBangKeVt, String trangThai, String tenTrangThai,Long keHoachDcDtlId) {
        this.id = id;
        this.qDinhDccId = qDinhDccId;
        this.soQdinh = soQdinh;
        this.ngayKyQdinh = ngayKyQdinh;
        this.namKh = namKh;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.thayDoiThuKho = thayDoiThuKho;
        this.soPhieuXuatKho = soPhieuXuatKho;
        this.ngayXuatKho = ngayXuatKho;
        this.phieuKiemNghiemId = phieuKiemNghiemId;
        this.soPhieuKiemNghiemCl = soPhieuKiemNghiemCl;
        this.ngayGiamDinh = ngayGiamDinh;
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.ktvBaoQuan = ktvBaoQuan;
        this.donViTinh = donViTinh;
        this.slDienChuyen = slDienChuyen;
        this.duToanKinhPhiDc = duToanKinhPhiDc;
        this.bKCanHangId = bKCanHangId;
        this.soBKCanHang = soBKCanHang;
        this.bangKeVtId = bangKeVtId;
        this.soBangKeVt = soBangKeVt;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
        this.keHoachDcDtlId=keHoachDcDtlId;
    }
}
