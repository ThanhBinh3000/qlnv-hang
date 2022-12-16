package com.tcdt.qlnvhang.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhChiTietTTinChaoGiaReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhQdPheduyetKhMttHdrReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Integer namKh;

    private String soQd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayQd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private Long idThHdr;

    private Long idTrHdr;

    private String soTrHdr;

    private String maDvi;

    private String trichYeu;

    private String trangThai;


    private String loaiVthh;

    private String cloaiVthh;

    private String tchuanCluong;

    private Boolean lastest = false;

    private String phanLoai;


    private String pthucMuatt;

    private String diaDiemCgia;

    private String ghiChu;

    private String soQdPdKqCg;

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();

    private List<HhQdPheduyetKhMttDxReq> dsDiaDiem = new ArrayList<>();

//    private List<HhChiTietTTinChaoGiaReq> hhChiTietTTinChaoGiaReqList = new ArrayList<>();
}
