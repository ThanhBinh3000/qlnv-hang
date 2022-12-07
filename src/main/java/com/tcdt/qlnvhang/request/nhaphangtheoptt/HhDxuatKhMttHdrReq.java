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
    private String diaChiDvi;
    private Integer namKh;
    private String soDxuat;
    private String trichYeu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private String tenDuAn;
    private String soQd;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String ptMua;
    private String tchuanCluong;
    private String giaMua;
    private String thueGtgt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMkho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKthuc;
    private String ghiChu;
    private BigDecimal tongMucDt;
    private BigDecimal tongSoLuong;
    private String nguonVon;

    private BigDecimal tongTienVat;
    private String trangThai;
    private String trangThaiTh;

    private String ldoTuchoi;

    private String maThop;

    private String noiDungTh;
    private BigDecimal donGia;
    private BigDecimal donGiaVat;

    private List<FileDinhKemReq> fileDinhKemReq =  new ArrayList<>();


    private List<HhDxuatKhMttSlddReq> dsSlddReq = new ArrayList<>();


    private List<HhDxuatKhMttCcxdgReq> ccXdgReq = new ArrayList<>();


}
