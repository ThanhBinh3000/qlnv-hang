package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class DcnbBBNTBQHdrDTO {
    private Long id;
    private Long qDinhDccId;
    private String soQdinh;
    private Integer namKh;
    private LocalDate thoiHanDieuChuyen;
    private String maNhaKhoXuat;
    private String tenNhaKhoXuat;
    private String maDiemKhoXuat;
    private String tenDiemKhoXuat;
    private String maLoKhoXuat;
    private String tenLoKhoXuat;
    private String maNganKhoXuat;
    private String tenNganKhoXuat;
    private String trangThaiXuat;
    private String tenTrangThaiXuat;
    private String maNhaKhoNhan;
    private String tenNhaKhoNhan;
    private String maDiemKhoNhan;
    private String tenDiemKhoNhan;
    private String maLoKhoNhan;
    private String tenLoKhoNhan;
    private String maNganKhoNhan;
    private String tenNganKhoNhan;
    private String trangThaiNhan;
    private String tenTrangThaiNhan;
    private String soBBKLot;
    private LocalDate ngayLapBBKLot;
    private LocalDate ngayKetThucNtKeLot;
    private BigDecimal tongKinhPhiTT;
    private BigDecimal tongKinhPhiPd;
    private String trangThai;
    private String tenTrangThai;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private BigDecimal tichLuongKd;
    private String loaiVthh;
    private String cloaiVthh;
    private String donViTinh;
    private Long keHoachDcDtlId;
    public DcnbBBNTBQHdrDTO(Long id, Long qDinhDccId, String soQdinh, Integer namKh, LocalDate thoiHanDieuChuyen, String maNhaKhoXuat, String tenNhaKhoXuat, String maDiemKhoXuat, String tenDiemKhoXuat, String maLoKhoXuat, String tenLoKhoXuat, String maNganKhoXuat, String tenNganKhoXuat, String maNhaKhoNhan, String tenNhaKhoNhan, String maDiemKhoNhan, String tenDiemKhoNhan, String maLoKhoNhan, String tenLoKhoNhan, String maNganKhoNhan, String tenNganKhoNhan, String soBBKLot, LocalDate ngayLapBBKLot, LocalDate ngayKetThucNtKeLot, BigDecimal tongKinhPhiTT, BigDecimal tongKinhPhiPd, String trangThai, String tenTrangThai, String tenLoaiVthh, String tenCloaiVthh, BigDecimal tichLuongKd, String loaiVthh, String cloaiVthh,  String donViTinh,Long keHoachDcDtlId) {
        this.id = id;
        this.qDinhDccId = qDinhDccId;
        this.soQdinh = soQdinh;
        this.namKh = namKh;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maNhaKhoXuat = maNhaKhoXuat;
        this.tenNhaKhoXuat = tenNhaKhoXuat;
        this.maDiemKhoXuat = maDiemKhoXuat;
        this.tenDiemKhoXuat = tenDiemKhoXuat;
        this.maLoKhoXuat = maLoKhoXuat;
        this.tenLoKhoXuat = tenLoKhoXuat;
        this.maNganKhoXuat = maNganKhoXuat;
        this.tenNganKhoXuat = tenNganKhoXuat;
        this.maNhaKhoNhan = maNhaKhoNhan;
        this.tenNhaKhoNhan = tenNhaKhoNhan;
        this.maDiemKhoNhan = maDiemKhoNhan;
        this.tenDiemKhoNhan = tenDiemKhoNhan;
        this.maLoKhoNhan = maLoKhoNhan;
        this.tenLoKhoNhan = tenLoKhoNhan;
        this.maNganKhoNhan = maNganKhoNhan;
        this.tenNganKhoNhan = tenNganKhoNhan;
        this.soBBKLot = soBBKLot;
        this.ngayLapBBKLot = ngayLapBBKLot;
        this.ngayKetThucNtKeLot = ngayKetThucNtKeLot;
        this.tongKinhPhiTT = tongKinhPhiTT;
        this.tongKinhPhiPd = tongKinhPhiPd;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
        this.tenLoaiVthh = tenLoaiVthh;
        this.tenCloaiVthh = tenCloaiVthh;
        this.tichLuongKd = tichLuongKd;
        this.loaiVthh = loaiVthh;
        this.cloaiVthh = cloaiVthh;
        this.donViTinh = donViTinh;
        this.keHoachDcDtlId=keHoachDcDtlId;
    }
}
