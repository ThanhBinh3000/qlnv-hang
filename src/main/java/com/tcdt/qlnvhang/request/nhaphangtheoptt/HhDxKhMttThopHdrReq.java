package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class HhDxKhMttThopHdrReq extends HhDxKhMttTChiThopReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idTh;
    @NotNull(message = "Không được để trống")
    @Size(max = 250, message = "Về việc không được vượt quá 250 ký tự")
    @ApiModelProperty(example = "Nội dung về việc")
    String noiDung;

    String soQdCc;

}


