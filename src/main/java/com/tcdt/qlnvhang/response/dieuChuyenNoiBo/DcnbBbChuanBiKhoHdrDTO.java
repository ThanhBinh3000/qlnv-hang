package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;

@Data
public class DcnbBbChuanBiKhoHdrDTO {
    private Long id;
    private Long qDinhDccId;
    private String soQdinh;
    private Integer namKh;
    private LocalDate thoiGianNhapKhoMuonNhat;
    private String soBbChuanBiKho;
    private LocalDate ngayBbChuanBiKho;
    private Long phieuNhapKhoId;
    private String soPhieuNhapKho;
    private Long bbKetThucNKId;
    private String soBbKetThucNK;
    private LocalDate ngayKtNhapKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maloKho;
    private String tenloKho;
    private String maNganKho;
    private String tenNganKho;
    private String trangThai;
    private String tenTrangThai;

    public DcnbBbChuanBiKhoHdrDTO(Long id, Long qDinhDccId, String soQdinh, Integer namKh, LocalDate thoiGianNhapKhoMuonNhat, String soBbChuanBiKho, LocalDate ngayBbChuanBiKho, Long phieuNhapKhoId, String soPhieuNhapKho, Long bbKetThucNKId, String soBbKetThucNK, LocalDate ngayKtNhapKho, String maDiemKho, String tenDiemKho, String maloKho, String tenloKho, String maNganKho, String tenNganKho, String trangThai, String tenTrangThai) {
        this.id = id;
        this.qDinhDccId = qDinhDccId;
        this.soQdinh = soQdinh;
        this.namKh = namKh;
        this.thoiGianNhapKhoMuonNhat = thoiGianNhapKhoMuonNhat;
        this.soBbChuanBiKho = soBbChuanBiKho;
        this.ngayBbChuanBiKho = ngayBbChuanBiKho;
        this.phieuNhapKhoId = phieuNhapKhoId;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.bbKetThucNKId = bbKetThucNKId;
        this.soBbKetThucNK = soBbKetThucNK;
        this.ngayKtNhapKho = ngayKtNhapKho;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maloKho = maloKho;
        this.tenloKho = tenloKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
