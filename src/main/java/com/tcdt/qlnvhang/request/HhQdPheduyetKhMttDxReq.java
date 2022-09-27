package com.tcdt.qlnvhang.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class HhQdPheduyetKhMttDxReq {
    @ApiModelProperty(notes = "bắt buộc set phải đối với updata")
    private Long id;
    Long idQdKhmtt;
    String maDvi;
    String tenDvi;
    String diaChi;
    String soKhoach;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayKy;
    String trichYeu;
    String tenDuAn;
    BigDecimal soLuong;
    BigDecimal tongTien;

}
