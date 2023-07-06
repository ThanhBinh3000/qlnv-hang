package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;

@Data
public class DcnbBienBanLayMauHdrDTO {
    private Long id;
    private Long qDDccId;
    private String soQdinh;
    private Integer namKh;
    private LocalDate thoiHanDieuChuyen;
    private String maDiemKho;
    private String tenDiemKho;
    private String maloKho;
    private String tenloKho;
    private String maNganKho;
    private String tenNganKho;
    private Boolean thayDoiThuKho;
    private String soBBLayMau;
    private LocalDate ngaylayMau;
    private String soBBTinhKho;
    private LocalDate ngayXuatDocKho;
    private String bbHaoDoi;
    private String trangThai;
    private String tenTrangThai;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String maNhaKho;
    private String tenNhaKho;
    private Long thuKhoId;
    private String thuKho;
    private Long thuKhoNhanId;
    private String thuKhoNhan;
    private String donViTinh;
    private String tenDonViTinh;

    public DcnbBienBanLayMauHdrDTO(Long id, Long qDDccId, String soQdinh, Integer namKh, LocalDate thoiHanDieuChuyen, String maDiemKho, String tenDiemKho, String maloKho, String tenloKho, String maNganKho, String tenNganKho, Boolean thayDoiThuKho, String soBBLayMau, LocalDate ngaylayMau, String soBBTinhKho, LocalDate ngayXuatDocKho, String bbHaoDoi, String trangThai, String tenTrangThai, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String maNhaKho, String tenNhaKho, Long thuKhoId, String thuKho, Long thuKhoNhanId, String thuKhoNhan, String donViTinh, String tenDonViTinh) {
        this.id = id;
        this.qDDccId = qDDccId;
        this.soQdinh = soQdinh;
        this.namKh = namKh;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maloKho = maloKho;
        this.tenloKho = tenloKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.thayDoiThuKho = thayDoiThuKho;
        this.soBBLayMau = soBBLayMau;
        this.ngaylayMau = ngaylayMau;
        this.soBBTinhKho = soBBTinhKho;
        this.ngayXuatDocKho = ngayXuatDocKho;
        this.bbHaoDoi = bbHaoDoi;
        this.trangThai = trangThai;
        this.tenTrangThai = tenTrangThai;
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.thuKhoId = thuKhoId;
        this.thuKho = thuKho;
        this.thuKhoNhanId = thuKhoNhanId;
        this.thuKhoNhan = thuKhoNhan;
        this.donViTinh = donViTinh;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
