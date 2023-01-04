package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhPhieuNhapKhoCtReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String donViTinh;

    private BigDecimal soLuongChungTu;

    private BigDecimal soLuongThucNhap;

    private BigDecimal donGia;

    private String maSo;


}
