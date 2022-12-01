package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhDxuatKhMttCcxdgReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    Long idHdr;

    @NotNull(message = "Không được để trống")
    @Size(max = 250, message = "Tên tài liệu không được vượt quá 250 ký tự")
    @ApiModelProperty(example = "Tailieumau")
    String tenTlieu;

    @NotNull(message = "Không được để trống")
    @Size(max = 2, message = "Loại căn cứ không được vượt quá 2 ký tự")
    @ApiModelProperty(example = "00")
    String loaiCanCu;

    List<FileDinhKemReq> children;
}
