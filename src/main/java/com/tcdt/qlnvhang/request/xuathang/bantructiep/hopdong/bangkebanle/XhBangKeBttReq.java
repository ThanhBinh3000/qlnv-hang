package com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.bangkebanle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class XhBangKeBttReq extends BaseRequest {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Integer namKh;

    private String soBangKe;

    private String soQd;

    private String maDvi;

    private BigDecimal soLuong;

    private BigDecimal soLuongConLai;

    private String nguoiPhuTrach;

    private String diaChi;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayBanHang;

    private String loaiVthh;

    private String cloaiVthh;

    private BigDecimal soLuongBtt;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String tenNguoiMua;

    private String diaChiNguoiMua;

    private String cmt;

    private String ghiChu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayBanHangTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayBanHangDen;

}
