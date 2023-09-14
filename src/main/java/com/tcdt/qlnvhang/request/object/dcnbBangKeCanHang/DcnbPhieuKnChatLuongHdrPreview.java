package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKnChatLuongDtlDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DcnbPhieuKnChatLuongHdrPreview {
    private String loaiHangHoa;
    private String maDvi;
    private String tenDvi;
    private String soPhieu;
    private String tenNganKho;
    private String tenLoKho;
    private String tenNhaKho;
    private String tenDiemKho;
    private String soLuongHangBaoQuan;
    private String hinhThucBq;
    private String tenThuKho;
    private String ngayNhapDayKho;
    private String ngayLayMau;
    private String ngayKiem;
    private String danhGiaCamQuan;
    private String nhanXetKetLuan;
    private int ngayNhap;
    private int thangNhap;
    private int namNhap;
    private String nguoiKt;
    private String truongBpKtbq;
    private String lanhDaoCuc;
    private String maQhns;
    private String hinhThucKeLot;
    List<DcnbPhieuKnChatLuongDtlDto> dcnbPhieuKnChatLuongDtls;
}
