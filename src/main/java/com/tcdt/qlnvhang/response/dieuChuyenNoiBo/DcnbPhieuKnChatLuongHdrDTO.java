package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DcnbPhieuKnChatLuongHdrDTO {
    private Long id;
    private Long bBLayMauId;
    private Long qDinhDccId;
    private String soQdinh;
    private LocalDate ngayHieuLucQd;
    private Integer nam;
    private LocalDate thoiHanDieuChuyen;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private Boolean thayDoiThuKho;
    private String soPhieuKnChatLuong;
    private LocalDate ngayKiemNghiem;
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
    private String thuKho;
    private String maNganKho;
    private String tenNganKho;
    private LocalDate ngayHieuLuc;
    private String donViTinh;
    private String tenDonViTinh;
    private Long thuKhoId;
    private Long thuKhoNhanId;
    private String thuKhoNhan;

    public DcnbPhieuKnChatLuongHdrDTO(Long id, Long bBLayMauId, Long qDinhDccId, String soQdinh, LocalDate ngayHieuLucQd, Integer nam, LocalDate thoiHanDieuChuyen, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, Boolean thayDoiThuKho, String soPhieuKnChatLuong, LocalDate ngayKiemNghiem, String soBBLayMau, LocalDate ngaylayMau, String soBBTinhKho, LocalDate ngayXuatDocKho, String bbHaoDoi, String trangThai, String tenTrangThai, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String maNhaKho, String tenNhaKho, String thuKho, String maNganKho, String tenNganKho, LocalDate ngayHieuLuc, String donViTinh, String tenDonViTinh, Long thuKhoId, Long thuKhoNhanId, String thuKhoNhan) {
        this.id = id;
        this.bBLayMauId = bBLayMauId;
        this.qDinhDccId = qDinhDccId;
        this.soQdinh = soQdinh;
        this.ngayHieuLucQd = ngayHieuLucQd;
        this.nam = nam;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.thayDoiThuKho = thayDoiThuKho;
        this.soPhieuKnChatLuong = soPhieuKnChatLuong;
        this.ngayKiemNghiem = ngayKiemNghiem;
        this.soBBLayMau = soBBLayMau;
        this.ngaylayMau = ngaylayMau;
        this.soBBTinhKho = soBBTinhKho;
        this.ngayXuatDocKho = ngayXuatDocKho;
        this.bbHaoDoi = bbHaoDoi;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.thuKho = thuKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.ngayHieuLuc = ngayHieuLuc;
        this.donViTinh = donViTinh;
        this.tenDonViTinh = tenDonViTinh;
        this.thuKhoId = thuKhoId;
        this.thuKhoNhanId = thuKhoNhanId;
        this.thuKhoNhan = thuKhoNhan;
    }

    public DcnbPhieuKnChatLuongHdrDTO(Long id, Long bBLayMauId, Long qDinhDccId, String soQdinh, LocalDate ngayHieuLucQd, Integer nam, LocalDate thoiHanDieuChuyen, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, Boolean thayDoiThuKho, String soPhieuKnChatLuong, LocalDate ngayKiemNghiem, String soBBLayMau, LocalDate ngaylayMau, String soBBTinhKho, LocalDate ngayXuatDocKho, String bbHaoDoi, String trangThai, String tenTrangThai, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String maNhaKho, String tenNhaKho, String thuKho, String maNganKho, String tenNganKho, LocalDate ngayHieuLuc, String donViTinh, String tenDonViTinh,Long thuKhoId) {
        this.id = id;
        this.bBLayMauId = bBLayMauId;
        this.qDinhDccId = qDinhDccId;
        this.soQdinh = soQdinh;
        this.ngayHieuLucQd = ngayHieuLucQd;
        this.nam = nam;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.thayDoiThuKho = thayDoiThuKho;
        this.soPhieuKnChatLuong = soPhieuKnChatLuong;
        this.ngayKiemNghiem = ngayKiemNghiem;
        this.soBBLayMau = soBBLayMau;
        this.ngaylayMau = ngaylayMau;
        this.soBBTinhKho = soBBTinhKho;
        this.ngayXuatDocKho = ngayXuatDocKho;
        this.bbHaoDoi = bbHaoDoi;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.thuKho = thuKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.ngayHieuLuc = ngayHieuLuc;
        this.donViTinh = donViTinh;
        this.tenDonViTinh = tenDonViTinh;
        this.thuKhoId = thuKhoId;
    }
}
