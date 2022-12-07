package com.tcdt.qlnvhang.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntDsgthauReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhQdPheduyetKhMttDxReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idHdr;
    private Long idDxHdr;
    //    @NotNull(message = "Không được để trống")
    @Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "HNO")
    private String maDvi;

//    @NotNull(message = "Không được để trống")
    @Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
    @ApiModelProperty(example = "Cục Hà Nội")
    private String tenDvi;

//    @NotNull(message = "Không được để trống")
    @Size(max = 20, message = "Số đề xuất không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "Tên dự án")
    private String soDxuat;


//    @NotNull(message = "Không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;

//    @NotNull(message = "Không được để trống")
    @Size(max = 250, message = "Tên dự án không được vượt quá 250 ký tự")
    @ApiModelProperty(example = "Tên dự án")
    private String tenDuAn;
    private BigDecimal tongSoLuong;
    @Size(max = 4, message = "Năm kế hoạch không được vượt quá 4 ký tự")
    @ApiModelProperty(example = "2022")
    private String namKh;
    private String trichYeu;
    private String diaChiDvi;
    private BigDecimal tongTienVat;

    private List<HhQdPheduyetKhMttSLDDReq> dsGoiThau;
    private List<HhQdPheduyetKhMttSLDDReq> children;

}
