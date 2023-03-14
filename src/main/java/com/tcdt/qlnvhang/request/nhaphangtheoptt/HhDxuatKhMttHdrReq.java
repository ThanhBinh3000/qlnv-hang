package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
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
public class HhDxuatKhMttHdrReq {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private String maDvi;

    private String loaiHinhNx;

    private String kieuNx;

    private String diaChi;

    private Integer namKh;

    private String soDxuat;

    private String trichYeu;

    private String tenDuAn;

    private String soQdCc
            ;
    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String ptMua;

    private String tchuanCluong;

    private String giaMua;

    private BigDecimal thueGtgt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKthuc;

    private String ghiChu;

    private BigDecimal tongMucDt;

    private BigDecimal tongSoLuong;

    private BigDecimal tongTienGomThue;

    private String nguonVon;

    private Long maThop;

    private BigDecimal donGiaVat;

    private String soQdPduyet;

    private String trangThaiTh;

    private List<FileDinhKemReq> fileDinhKemReq =  new ArrayList<>();


    private List<HhDxuatKhMttSlddReq> children = new ArrayList<>();


    private List<HhDxuatKhMttCcxdgReq> ccXdgReq = new ArrayList<>();


}
