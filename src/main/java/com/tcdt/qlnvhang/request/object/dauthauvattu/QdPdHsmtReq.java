package com.tcdt.qlnvhang.request.object.dauthauvattu;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class QdPdHsmtReq {
    private Long id;
    private Integer namKhoach;
    private String soQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;
    private Long idQdPdKhlcnt;
    private String trichYeu;
    private String noiDungQd;
    private String ghiChu;
    private String loaiVthh;
    private Long idQdPdKhlcntDtl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMthau;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDthau;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
    private Date tgianDthauTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
    private Date tgianMthauTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianBdauTchuc;
    private List<FileDinhKemReq> listCcPhapLy;
}
