package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;

@Data
public class DcnbBbGiaoNhanHdrDTO {
    private Long id;
    private Long qdDcCucId;
    private String soQdinh;
    private Integer namKh;
    private LocalDate thoiHanDieuChuyen;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maloKho;
    private String tenloKho;
    private String maNganKho;
    private String tenNganKho;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String donViTinh;
    private String tenDonViTinh;

    private String soHoSoKt;
    private Long hoSoKtId;
    private String soBienBanGiaoNhan;
    private String soBienBanKetThucNk;
    private Long bienBanKetThucNkId;
    private LocalDate ngayKetThucNk;
    private String soBienBanLayMau;
    private Long bienBanLayMauId;
    private String trangThai;
    private String tenTrangThai;

    public DcnbBbGiaoNhanHdrDTO(Long id, Long qdDcCucId, String soQdinh, Integer namKh, LocalDate thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maloKho, String tenloKho, String maNganKho, String tenNganKho, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String donViTinh, String tenDonViTinh, String soHoSoKt, Long hoSoKtId, String soBienBanGiaoNhan, String soBienBanKetThucNk, Long bienBanKetThucNkId, LocalDate ngayKetThucNk, String soBienBanLayMau, Long bienBanLayMauId, String trangThai, String tenTrangThai) {
        this.id = id;
        this.qdDcCucId = qdDcCucId;
        this.soQdinh = soQdinh;
        this.namKh = namKh;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maloKho = maloKho;
        this.tenloKho = tenloKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.donViTinh = donViTinh;
        this.tenDonViTinh = tenDonViTinh;
        this.soHoSoKt = soHoSoKt;
        this.hoSoKtId = hoSoKtId;
        this.soBienBanGiaoNhan = soBienBanGiaoNhan;
        this.soBienBanKetThucNk = soBienBanKetThucNk;
        this.bienBanKetThucNkId = bienBanKetThucNkId;
        this.ngayKetThucNk = ngayKetThucNk;
        this.soBienBanLayMau = soBienBanLayMau;
        this.bienBanLayMauId = bienBanLayMauId;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
