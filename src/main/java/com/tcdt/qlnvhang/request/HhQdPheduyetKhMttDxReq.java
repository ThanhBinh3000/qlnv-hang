package com.tcdt.qlnvhang.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhQdPheduyetKhMttDxReq {
    @ApiModelProperty(notes = "bắt buộc set phải đối với updata")
    private Long id;
    private Long idDxuat;
    private Long idPduyetHdr;
    private String maDvi;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String ptMua;
    private String tchuanCluong;
    private BigDecimal giaMua;
    private BigDecimal giaChuaThue;
    private BigDecimal giaCoThue;
    private BigDecimal thueGtgt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMkho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKthuc;
    private String ghiChu;
    private BigDecimal tongMucDt;
    private BigDecimal tongSoLuong;
    private String nguonVon;
    private String tenChuDt;
    private String trichYeu;
    private String tenDuAn;
    @Temporal(TemporalType.DATE)
    private Date ngayKy;
    private List<HhQdPheduyetKhMttSLDDReq> hhQdPheduyetKhMttSLDDList = new ArrayList<>();

}
