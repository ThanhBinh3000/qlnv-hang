package com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.bangkebanle;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhBangKeBttReq extends BaseRequest {

    private Long id;

    private Integer namKh;

    private String soBangKe;

    private String soQd;

    private String maDvi;

    private BigDecimal soLuong;

    private BigDecimal soLuongConLai;

    private String nguoiPhuTrach;

    private String diaChi;

    private LocalDate ngayBanHang;

    private String loaiVthh;

    private String cloaiVthh;

    private BigDecimal soLuongBtt;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String tenNguoiMua;

    private String diaChiNguoiMua;

    private String cmt;

    private String ghiChu;

    private LocalDate ngayBanHangTu;

    private LocalDate ngayBanHangDen;
}
