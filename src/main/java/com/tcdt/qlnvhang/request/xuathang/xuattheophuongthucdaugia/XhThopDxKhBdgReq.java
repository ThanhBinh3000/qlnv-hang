package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgDtl;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class XhThopDxKhBdgReq extends XhThopChiTieuReq{
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
