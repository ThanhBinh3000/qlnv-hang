package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class XhThopDxKhBttHdrReq extends XhThopChiTieuReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idTh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayThop;
    private String maDvi;
    private String noiDungThop;
    private String trangThai;
    private String soQdPd;
}
