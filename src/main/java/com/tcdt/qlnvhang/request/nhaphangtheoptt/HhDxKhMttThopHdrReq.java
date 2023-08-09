package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class HhDxKhMttThopHdrReq extends HhDxKhMttTChiThopReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idTh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayThop;

    private String maDvi;

    private String noiDungThop;

    private String trangThai;

    private String soQdPd;

    private String soQdCc;
    private Long idSoQdCc;
    private String tchuanCluong;
    private List<FileDinhKemReq> fileDinhKems;
}


