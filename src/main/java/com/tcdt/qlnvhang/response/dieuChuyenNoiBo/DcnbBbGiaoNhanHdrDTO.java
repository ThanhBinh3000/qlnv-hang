package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DcnbBbGiaoNhanHdrDTO {
    private Long id;
    private Long qdDcCucId;
    private String soQdinh;
    private LocalDate ngayKyQd;
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
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private BigDecimal soLuongDc;
    private String donViTinh;

    private String soHoSoKt;
    private Long hoSoKtId;
    private String soBienBanGiaoNhan;
    private String soBienBanKetThucNk;
    private Long bienBanKetThucNkId;
    private LocalDate ngayKetThucNk;
    private String soBienBanLayMau;
    private String trangThai;
    private String tenTrangThai;

    public DcnbBbGiaoNhanHdrDTO(Long id, Long qdDcCucId, String soQdinh,LocalDate ngayKyQd, Integer namKh, LocalDate thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa,BigDecimal soLuongDc, String donViTinh, String soHoSoKt, Long hoSoKtId, String soBienBanGiaoNhan, String soBienBanKetThucNk, Long bienBanKetThucNkId, LocalDate ngayKetThucNk, String soBienBanLayMau,String trangThai, String tenTrangThai) {
        this.id = id;
        this.qdDcCucId = qdDcCucId;
        this.soQdinh = soQdinh;
        this.ngayKyQd = ngayKyQd;
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
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.soLuongDc = soLuongDc;
        this.donViTinh = donViTinh;
        this.soHoSoKt = soHoSoKt;
        this.hoSoKtId = hoSoKtId;
        this.soBienBanGiaoNhan = soBienBanGiaoNhan;
        this.soBienBanKetThucNk = soBienBanKetThucNk;
        this.bienBanKetThucNkId = bienBanKetThucNkId;
        this.ngayKetThucNk = ngayKetThucNk;
        this.soBienBanLayMau = soBienBanLayMau;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
