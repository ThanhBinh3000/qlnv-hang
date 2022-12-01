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
    private Date ngayTao;

//    @NotNull(message = "Không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;

//    @NotNull(message = "Không được để trống")
    @Size(max = 250, message = "Tên dự án không được vượt quá 250 ký tự")
    @ApiModelProperty(example = "Tên dự án")
    private String tenDuAn;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private BigDecimal donGiaTamTinh;
    private BigDecimal tongTien;

//    @NotNull(message = "Không được để trống")
    @Size(max = 4, message = "Năm kế hoạch không được vượt quá 4 ký tự")
    @ApiModelProperty(example = "2022")
    private String namKh;
    private Long idDxHdr;
    private String diaChiDvi;
    private String trichYeu;
    String cloaiVthh;

    String loaiVthh;



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

    @Temporal(TemporalType.DATE)
    private Date ngayKy;

    private List<HhQdPheduyetKhMttSLDDReq> dsGoiThau;
    private List<HhQdPheduyetKhMttSLDDReq> children;

}
