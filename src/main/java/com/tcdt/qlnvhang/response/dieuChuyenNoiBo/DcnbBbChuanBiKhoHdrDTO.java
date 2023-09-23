package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DcnbBbChuanBiKhoHdrDTO {
    private Long id;
    private Long qDinhDccId;
    private String soQdinh;
    private LocalDate ngayKyQd;
    private Integer namKh;
    private LocalDate thoiGianNhapKhoMuonNhat;
    private String soBbChuanBiKho;
    private LocalDate ngayBbChuanBiKho;
    private Long phieuNhapKhoId;
    private String soPhieuNhapKho;
    private Long bbKetThucNKId;
    private String soBbKetThucNK;
    private LocalDate ngayKtNhapKho;
    private Long bbGiaoNhanId;
    private String soBbGiaoNhan;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private BigDecimal soLuongDc;

    private String trangThai;
    private String tenTrangThai;
    private String donViTinh;
    private BigDecimal soLuongQdDcCuc;
    private Long keHoachDcDtlId;
    private BigDecimal duToanKphi;
    public DcnbBbChuanBiKhoHdrDTO(Long id, Long qDinhDccId, String soQdinh, LocalDate ngayKyQd, Integer namKh, LocalDate thoiGianNhapKhoMuonNhat, String soBbChuanBiKho, LocalDate ngayBbChuanBiKho, Long phieuNhapKhoId, String soPhieuNhapKho, Long bbKetThucNKId, String soBbKetThucNK, LocalDate ngayKtNhapKho, Long bbGiaoNhanId, String soBbGiaoNhan, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, BigDecimal soLuongDc, String trangThai,String donViTinh,BigDecimal soLuongQdDcCuc,Long keHoachDcDtlId,BigDecimal duToanKphi) {
        this.id = id;
        this.qDinhDccId = qDinhDccId;
        this.soQdinh = soQdinh;
        this.ngayKyQd = ngayKyQd;
        this.namKh = namKh;
        this.thoiGianNhapKhoMuonNhat = thoiGianNhapKhoMuonNhat;
        this.soBbChuanBiKho = soBbChuanBiKho;
        this.ngayBbChuanBiKho = ngayBbChuanBiKho;
        this.phieuNhapKhoId = phieuNhapKhoId;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.bbKetThucNKId = bbKetThucNKId;
        this.soBbKetThucNK = soBbKetThucNK;
        this.ngayKtNhapKho = ngayKtNhapKho;
        this.bbGiaoNhanId = bbGiaoNhanId;
        this.soBbGiaoNhan = soBbGiaoNhan;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.soLuongDc = soLuongDc;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
        this.donViTinh = donViTinh;
        this.soLuongQdDcCuc= soLuongQdDcCuc;
        this.keHoachDcDtlId = keHoachDcDtlId;
        this.duToanKphi= duToanKphi;
    }
}
