package com.tcdt.qlnvhang.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.persistence.Transient;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhQdPheduyetKhMttDxReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idQdHdr;

    private Long idDxHdr;

    private String maDvi;
    private Long idSoQdCc;
    private String soQdCc;
    private String diaChi;

    private String soDxuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;

    private String trichYeu;

    private String tenDuAn;

    private BigDecimal tongSoLuong;

    private BigDecimal tongTienGomThue;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String ptMua;

    private String tchuanCluong;

    private String giaMua;

    private BigDecimal donGia;

    private BigDecimal thueGtgt;

    private BigDecimal donGiaVat;

    private String ghiChu;

    private String nguonVon;

    private BigDecimal tongMucDt;

    private Integer namKh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKthuc;

    @Transient
    private List<HhQdPheduyetKhMttSLDDReq> children = new ArrayList<>();

}
