package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhDxuatKhMttHdrReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    @NotNull(message = "Không được để trống")
    private String loaiHinhNx;
    @NotNull(message = "Không được để trống")
    private String kieuNx;
    private String tenDuAn;
    private Integer namKh;
    @NotNull(message = "Không được để trống")
    private String soDxuat;
    @NotNull(message = "Không được để trống")
    private String trichYeu;
    private String soQd;
    private String trangThai;
    private String trangThaiTh;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String ptMua;
    private String tchuanCluong;
    private String giaMua;
    private String giaChuaThue;
    private String giaCoThue;
    private String thueGtgt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMkho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKthuc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private String ghiChu;
    private BigDecimal tongMucDt;
    private BigDecimal tongSoLuong;
    private String nguonVon;
    private String tenChuDt;
    private String maThop;
    private String diaChiDvi;
    private String noiDungTh;

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();


    private List<HhDxuatKhMttSlddReq> soLuongDiaDiemList = new ArrayList<>();


    private List<HhDxuatKhMttCcxdgReq> ccXdgList = new ArrayList<>();


}
