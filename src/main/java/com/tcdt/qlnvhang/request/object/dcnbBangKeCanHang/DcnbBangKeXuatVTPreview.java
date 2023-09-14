package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeXuatVTDtlDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DcnbBangKeXuatVTPreview {
    private String tenDvi;
    private String maQhns;
    private String soBangKe;
    private String tenThuKho;
    private String tenNganKho;
    private String tenLoKho;
    private String tenDiemKho;
    private String cloaiVthh;
    private String donViTinh;
    private String hoVaTenNguoiNhanHang;//Họ và tên người nhận hàng
    private String thoiHanGiaoNhan;
    private String ngayThangXuat; // Ngày tháng xuất
    private String nguoiGiamSat; //Người giám sát
    private Long tongSlHang; // Tổng số lượng hàng
    private String tongSlHangBc; // Tổng số lượng hàng viết bằng chữ
    private int ngayNhap;
    private int thangNhap;
    private int namNhap;
    private List<DcnbBangKeXuatVTDtlDto> dcnbBangKeXuatVTDtlDto;
}
