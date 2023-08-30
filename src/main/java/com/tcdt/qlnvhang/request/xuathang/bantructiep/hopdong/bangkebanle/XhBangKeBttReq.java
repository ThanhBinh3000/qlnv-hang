package com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.bangkebanle;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhBangKeBttReq extends BaseRequest {
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String soBangKe;
    private String idQdNv;
    private String soQdNv;
    private BigDecimal soLuongBanTrucTiep;
    private BigDecimal soLuongConLai;
    private String nguoiPhuTrach;
    private String diaChi;
    private LocalDate ngayBanHang;
    private String loaiVthh;
    private String cloaiVthh;
    private BigDecimal soLuongBanLe;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String tenNguoiMua;
    private String diaChiNguoiMua;
    private String cmt;
    private String ghiChu;
    private LocalDate ngayTao;
    private Long nguoiTaoId;
    private LocalDate ngayBanHangTu;
    private LocalDate ngayBanHangDen;
    private String dvql;
}
