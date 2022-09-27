package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class HhDxKhMttTChiThopReq {

    @ApiModelProperty(example = "2022")
    Integer namKh;


    @ApiModelProperty(example = Contains.LOAI_VTHH_THOC)
    String loaiVthh;


    @ApiModelProperty(example = Contains.LOAI_VTHH_THOC)
    String cloaiVthh;
    String noiDung;
}
