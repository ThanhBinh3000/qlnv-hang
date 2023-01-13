package com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.bangkethumuale;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BangKeMuaLeReq extends BaseRequest {

    private Long id;
    private String soBangKe;
    private String maDvi;
    @Transient
    private String tenDvi;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    private BigDecimal soLuongQd;
    private BigDecimal soLuongConLai;
    private Integer namQd;
    private String nguoiMua;
    private String diaChiThuMua;
    private LocalDate ngayMua;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private BigDecimal soLuongMtt;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String nguoiBan;
    private String diaChiNguoiBan;
    private String soCmt;
    private String ghiChu;
    private String nguoiTao;

}
