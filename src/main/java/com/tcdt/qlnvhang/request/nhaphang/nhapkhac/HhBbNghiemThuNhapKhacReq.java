package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhBbanNghiemThuDtlReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class HhBbNghiemThuNhapKhacReq {
    private Long id;
    private Integer namKhoach;
    private String maDvi;
    private String maQhns;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    private String soBbNtBq;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNghiemThu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianNkho;;
    private String loaiVthh;
    private String cloaiVthh;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String pthucBquan;
    private String hthucBquan;
    private String ketLuan;
    private BigDecimal dinhMucGiao;
    private BigDecimal dinhMucThucTe;
    private List<FileDinhKemReq> fileDinhKems;
    private List<HhBbanNghiemThuDtlReq> detail;

    private ReportTemplateRequest reportTemplateRequest;
}
