package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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


    private String soDxuat;


    private String loaiVthh;


    private String soQd;


    private String trichYeu;



    private String maDvi;

    private String trangThai;
    private String trangThaiTh;


    private String ldoTuchoi;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;


    private Integer namKh;


    private String ghiChu;


    private String cloaiVthh;

    private String moTaHangHoa;


    private String tenDuAn;

    private BigDecimal tongMucDt;



    private String tchuanCluong;


    private String nguonVon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKthuc;

    private String diaChiDvi;


    private String loaiHinhNx;

    private String kieuNx;

    private BigDecimal donGiaVat;

    private String ptMua;

    private String giaMua;
    private String giaChuaThue;

    private String thueGtgt;

    private BigDecimal tongSoLuong;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;


    private String maThop;

    private String noiDungTh;

    private List<FileDinhKemReq> fileDinhKemReq =  new ArrayList<>();


    private List<HhDxuatKhMttSlddReq> dsSlddReq = new ArrayList<>();


    private List<HhDxuatKhMttCcxdgReq> ccXdgReq = new ArrayList<>();


}
