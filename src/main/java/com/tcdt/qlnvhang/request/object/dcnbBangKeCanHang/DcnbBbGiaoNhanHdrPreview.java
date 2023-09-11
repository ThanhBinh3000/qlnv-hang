package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DcnbBbGiaoNhanHdrPreview {
    private String chungLoaiHangHoa; //Chủng loại hàng DTQG
    private String theoHopDongSo; // Theo hợp đồng số
    private String ngayKyHopDong; //Ngày ký hợp đồng
    private String donViCungCapHang; //Đơn vị cung cấp hàng
    private String quyChuanTieuChuan;//Quy chuẩn, tiêu chuẩn
    private String ngayLap;
    private String chiCuc; //Chi cục
    private String tenDvi;
    private String tenLanhDao;
    private String tenCanBo;
    private String tenLanhDaoChiCuc; //Lãnh đạo Chi cục
    private String hoVatenDvCungCapHang; //Họ và tên Đơn vị cung cấp hàng
    private String chucVuDonViCungCapHang; //Chức vụ Đơn vị cung cấp hàng
    private String tongSoLuongThucNhap; //Tổng số lượng thực nhập
    private String dviTinh;
    private String tenDiemKho;
    private String daiDienDonViCungCapHang;//ĐẠI DIỆN ĐƠN VỊ CUNG CẤP HÀNG
    private String truongBpKtbq;//Trưởng BP KTBQ
    private String daiDienCtyThienHoaAn;//CÔNG TY TNHH THIÊN HÒA AN
    private String daiDienCucDtnnVinhPhu;//CỤC DTNN KHU VỰC VĨNH PHÚ
    private String daiDienChiCucDtnnVietTri;//CHI CỤC DTNN VIỆT TRÌ
}
