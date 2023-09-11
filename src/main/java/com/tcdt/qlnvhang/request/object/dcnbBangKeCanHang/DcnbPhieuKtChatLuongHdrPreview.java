package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKtChatLuongDtl;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DcnbPhieuKtChatLuongHdrPreview {
    private String tenDvi;
    private String maDvi;
    private String maQhns;
    private String loaiHangHoa; //Loại hàng DTQG
    private String soPhieu;
    private String nguoiGiaoHang;
    private String dVGiaoHang;
    private String diaChiDonViGiaoHang;
    private String theoHopDongSo; // Theo hợp đồng số
    private String ngayKyHopDong; //Ngày ký hợp đồng
    private String chungLoaiHangHoa; //Chủng loại hàng DTQG
    private String soChungThuGiamDinh;
    private String ngayGiamDinh;
    private String toChucGiamDinh;
    private BigDecimal slNhapTheoKb;
    private BigDecimal slNhapTheoKt;
    private String ngayLapPhieu;
    private String tenNganKho;
    private String tenLoKho;
    private String tenDiemKho;
    private String bienSoXe;
    private String danhGiaCamQuan;
    private String nhanXetKetLuan;
    private int ngayNhap;
    private int thangNhap;
    private int namNhap;
    private String ktvBaoQuan;//KTV bảo quản
    private String tenThuKho;
    private String tenLanhDaoChiCuc; //Lãnh đạo Chi cục
    private List<DcnbPhieuKtChatLuongDtl> dcnbPhieuKtChatLuongDtl;
}
