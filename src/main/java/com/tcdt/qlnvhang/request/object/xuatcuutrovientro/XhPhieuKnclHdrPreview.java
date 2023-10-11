package com.tcdt.qlnvhang.request.object.xuatcuutrovientro;

import com.tcdt.qlnvhang.response.xuatcuutrovientro.XhPhieuKnclDtlDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class XhPhieuKnclHdrPreview {
    private String tenDvi;
    private String maQhns;
    private String loaiVthh;
    private String soBbQd;
    private String tenNganKho;
    private String tenLoKho;
    private String tenNhaKho;
    private String tenDiemKho;
    private String soLuongHangBaoQuan;
    private String hinhThucKeLotBaoQuan;
    private String tenThuKho;
    private String ngayNhapDayKho;
    private String ngayBbLayMau;
    private String ngayKiemNghiem;
    private String ketLuan;
    private String ngayNhap;
    private String thangNhap;
    private String namNhap;
    private String nguoiLapPhieu;
    private String ktvBaoQuan;
    private String lanhDaoCuc;
    private String ketQua;
    private List<XhPhieuKnclDtlDto> xhPhieuKnclDtlDto;
}
