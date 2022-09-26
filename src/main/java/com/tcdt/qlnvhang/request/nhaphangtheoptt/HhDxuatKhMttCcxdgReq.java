package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HhDxuatKhMttCcxdgReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idDxKhmtt;
    private String moTa;
    private List<FileDinhKemReq> ccFileDinhkems =new ArrayList<>();
}
