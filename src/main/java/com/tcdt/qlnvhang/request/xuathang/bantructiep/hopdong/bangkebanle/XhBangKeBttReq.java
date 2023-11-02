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
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private BigDecimal slXuatBanQdPd;
    private BigDecimal soLuongConLai;
    private String nguoiPhuTrach;
    private String diaChi;
    private LocalDate ngayBanHang;
    private String loaiVthh;
    private String cloaiVthh;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String tenBenMua;
    private String diaChiBenMua;
    private String cmtBenMua;
    private String ghiChu;
    private LocalDate ngayTao;
    private Long nguoiTaoId;
    private LocalDate ngayBanHangTu;
    private LocalDate ngayBanHangDen;
    private String dvql;
}