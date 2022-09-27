package com.tcdt.qlnvhang.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttDx;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttSLDD;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhQdPheduyetKhMttHdrReq {
    @ApiModelProperty(notes = "bắt buộc set phải đối với update")
    private Long id;

    private Long namKhoach;

    @NotNull(message = "Không được để trống")
    @Size(max = 50, message = "số quyết định không được vượt quá 50 kí tự ")
    private String soQd;

    private String soDxuat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private Date ngayTao;


    private String nguoiTao;
    private Date ngaySua;


    private String nguoiSua;


    private String lyDoTuChoi;

    private Date ngayGuiDuyet;


    private  String nguoiGuiDuyet;

    private Date ngayPduyet;


    private String nguoiPduyet;


    private String maTongHop;


    private String trichYeu;


    private String loaiVthh;


    private String cloaiVthh;


    private String moTaHhoa;


    private String pThucMua;


    private String tieuChuanCl;


    private Long giaMua;


    private Long giaChuaVat;


    private Long thueGTGT;


    private  Long giaDaVat;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thoiDiemMoKho;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private  Date thoiDiemMuaThoc;


    private String ghiChu;


    private  String maDvi;

    private String tenDvi;


    private  Long tongMucDt;


    private  Long tongSlMuaTt;


    private String nguonVon;


    private String trangThai;



    private String trangThaiTh;



    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();


    private List<HhQdPheduyetKhMttDxReq> hhQdPheduyetKhMttDxList = new ArrayList<>();


    private List<HhQdPheduyetKhMttSLDDReq> hhQdPheduyetKhMttSLDDList = new ArrayList<>();
}
