package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauDtlDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DcnbBienBanLayMauHdrPreview {
    private String chungLoaiHangHoa;
    private String donViCungCapHang;
    private String quyChuanTieuChuan;
    private String ngayLayMau;
    private String tenDvi;
    private String tenDviCha;
    private Long soLuongMau;
    private String donViTinh;
    private String pPLayMau;
    private String chiTieuKiemTra;
    private String ktvBaoQuan;
    private String truongBpKtbq;
    private String lanhDaoChiCuc;
    private List<DcnbBienBanLayMauDtlDto> dcnbBienBanLayMauDtl;
    private String soQdinhDcc;
    private String ngayKyQDCC;
    private String soBbLayMau;
    private String tenThuKho;
}
