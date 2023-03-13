package com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.bienbanlaymau;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class XhBbLayMauBttDtlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private String loaiDaiDien;

    private String daiDien;
}
