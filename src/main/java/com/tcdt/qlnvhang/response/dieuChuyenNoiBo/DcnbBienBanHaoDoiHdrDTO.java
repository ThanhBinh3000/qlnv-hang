package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;

@Data
public class DcnbBienBanHaoDoiHdrDTO {
    private Long id;
    private Long bangKeCanHangId;
    private String soBbTinhKho;
    private Long bbTinhKhoId;
    private Long qDinhDcId;
    private Long phieuXuatKhoId;
    private String soQdinh;
    private Integer nam;
    private LocalDate ngayHieuLuc;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String soBienBanHaoDoi;
    private String soPhieuXuatKho;
    private String soBangKeXuatDcLt;
    private LocalDate ngayXuatKho;
    private String trangThai;
    private String tenTrangThai;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private String maNhaKho;
    private String tenNhaKho;
    private String donViTinh;
    private String maNganKho;
    private String tenNganKho;
    private LocalDate ngayKyQDinh;
    private LocalDate ngayBatDauXuat;
    private LocalDate ngayKetThucXuat;
    private Long keHoachDcDtlId;
    public DcnbBienBanHaoDoiHdrDTO(Long id, Long bangKeCanHangId,String soBbTinhKho, Long bbTinhKhoId, Long qDinhDcId, Long phieuXuatKhoId, String soQdinh, Integer nam, LocalDate ngayHieuLuc, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String soBienBanHaoDoi, String soPhieuXuatKho, String soBangKeXuatDcLt, LocalDate ngayXuatKho, String trangThai, String tenTrangThai, String loaiVthh, String tenLoaiVthh, String cloaiVthh, String tenCloaiVthh, String maNhaKho, String tenNhaKho, String donViTinh, String maNganKho, String tenNganKho, LocalDate ngayKyQDinh,Long keHoachDcDtlId) {
        this.id = id;
        this.bangKeCanHangId = bangKeCanHangId;
        this.soBbTinhKho= soBbTinhKho;
        this.bbTinhKhoId = bbTinhKhoId;
        this.qDinhDcId = qDinhDcId;
        this.phieuXuatKhoId = phieuXuatKhoId;
        this.soQdinh = soQdinh;
        this.nam = nam;
        this.ngayHieuLuc = ngayHieuLuc;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.soBienBanHaoDoi = soBienBanHaoDoi;
        this.soPhieuXuatKho = soPhieuXuatKho;
        this.soBangKeXuatDcLt = soBangKeXuatDcLt;
        this.ngayXuatKho = ngayXuatKho;
        this.trangThai = trangThai;
        this.tenTrangThai = tenTrangThai;
        this.loaiVthh = loaiVthh;
        this.tenLoaiVthh = tenLoaiVthh;
        this.cloaiVthh = cloaiVthh;
        this.tenCloaiVthh = tenCloaiVthh;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.donViTinh = donViTinh;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.ngayKyQDinh = ngayKyQDinh;
        this.keHoachDcDtlId = keHoachDcDtlId;
    }
}
