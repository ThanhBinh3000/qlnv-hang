package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKnChatLuongDtlDto;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKnChatLuongDtl;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DcnbPhieuKnChatLuongHdrPreview {
    private String loaiHangHoa; //Loại hàng DTQG
    private String maDvi;
    private String tenDvi;
    private String soPhieu;
    private String tenNganKho;
    private String tenLoKho;
    private String tenNhaKho;
    private String tenDiemKho;
    private String soLuongHangBaoQuan;//Số lượng hàng bảo quản
    private String hinhThucBq;
    private String tenThuKho;
    private String ngayNhapDayKho;//Ngày nhập đầy kho
    private String ngayLayMau;
    private String ngayKiem;
    private String danhGiaCamQuan;
    private String nhanXetKetLuan;
    private int ngayNhap;
    private int thangNhap;
    private int namNhap;
    private String nguoiKt;
    private String truongBpKtbq;//Trưởng BP KTBQ
    private String lanhDaoCuc; //Lãnh đạo Cục
    private String maQhns;
    private String hinhThucKeLot; //Hình thức kê lót
    List<DcnbPhieuKnChatLuongDtlDto> dcnbPhieuKnChatLuongDtls;
}
