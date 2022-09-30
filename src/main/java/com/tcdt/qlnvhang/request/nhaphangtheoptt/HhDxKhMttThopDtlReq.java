package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class HhDxKhMttThopDtlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idThopHdr;
    private Long idDxHdr;
    private String maDvi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDxuat;
    private String tenDuAn;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal tongTien;
}
