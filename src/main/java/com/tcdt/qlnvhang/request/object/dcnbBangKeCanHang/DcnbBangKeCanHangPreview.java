package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeCanHangDtlDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DcnbBangKeCanHangPreview {
    private String maDvi;
    private String maQhns;
    private String soBangKe;
    private String tenThuKho;
    private String lhKho; // Loai hình kho
    private String tenNganKho;
    private String tenLoKho;
    private String tenDiemKho;
    private String tenDvi;
    private String chungLoaiHangHoa; //Chủng loại hàng DTQG
    private String tenDonViTinh;
    private String tenNguoiGiaoHang;
    private String thoiGianGiaoNhan;
    private String nguoiGiamSat; //Người giám sát
    private List<DcnbBangKeCanHangDtlDto> dcnbBangKeCanHangDtl;
    private BigDecimal tongTrongLuongCabaoBi;
    private BigDecimal tongTrongLuongBaoBi;
    private BigDecimal tongTrongLuongTruBi;
    private String tongTrongLuongTruBiText;
    private int ngayNhap;
    private int thangNhap;
    private int namNhap;

}
