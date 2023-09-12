package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauDtlDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DcnbBienBanLayMauHdrPreview {

    private String chungLoaiHangHoa; //Chủng loại hàng DTQG
    private String donViCungCapHang; //Đơn vị cung cấp hàng
    private String quyChuanTieuChuan;//Quy chuẩn, tiêu chuẩn
    private String ngayLayMau;
    private String tenDvi;
    private String tenDviCha; // tên đơn vị cha
    private Long soLuongMau;
    private String donViTinh;
    private String pPLayMau;
    private String chiTieuKiemTra;
    private String ktvBaoQuan;
    private String truongBpKtbq;//Trưởng BP KTBQ
    private String lanhDaoChiCuc;//Lãnh đạo Chi cục
    private List<DcnbBienBanLayMauDtlDto> dcnbBienBanLayMauDtl;

}
