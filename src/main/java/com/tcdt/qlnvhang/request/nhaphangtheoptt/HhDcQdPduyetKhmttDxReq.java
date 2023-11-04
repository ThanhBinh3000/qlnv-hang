package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhDcQdPduyetKhmttDxReq {
    private Long id;
    private Long idDxHdr;
    private Long idQdHdr;
    private String soDxuat;
    private Long idDcHdr;
    private String maDvi;
    private String namKh;
    @Transient
    private String tenDvi;
    private String diaChiDvi;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String ptMua;
    private String tchuanCluong;
    private String giaMua;
    private BigDecimal donGia;
    private BigDecimal donGiaVat;
    private BigDecimal thueGtgt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMkho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKthuc;
    private String ghiChu;
    private BigDecimal tongMucDt;
    private BigDecimal tongSl;
    private BigDecimal tongSoLuong;
    private String nguonVon;
    private String tenChuDt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;
    private String trichYeu;
    private String tenDuAn;


    private List<HhDcQdPduyetKhmttSlddReq> children =new ArrayList<>();


}
