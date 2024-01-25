package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

@Data
public class HhDcQdPduyetKhmttHdrReq {
    private Long id;
    private String soQdDc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyDc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdGoc;
    private String trichYeu;
    private String trichYeuDc;
    private Long idQdGoc;
    private String soQdGoc;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String trangThai;
    private Integer namKh;
    private String maDvi;
    private Long soLanDieuChinh;
    private Long idSoQdCc;
    private String soQdCc;
    private String soToTrinh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoCv;
    private String loaiHinhNx;
    private String kieuNx;
    private String noiDungToTrinh;
    private String noiDungQdDc;

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();


    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();
    private List<FileDinhKemReq> cvanToTrinh = new ArrayList<>();

    private List<HhDcQdPduyetKhmttDxReq> hhDcQdPduyetKhmttDxList=new ArrayList<>();


//    đánh dấu có thay đổi ở QD
    private Boolean isChange;

    private ReportTemplateRequest reportTemplateRequest;
}
