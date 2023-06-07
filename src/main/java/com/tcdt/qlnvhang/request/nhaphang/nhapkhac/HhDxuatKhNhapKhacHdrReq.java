package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
public class HhDxuatKhNhapKhacHdrReq {
    private Long id;
    private Integer namKhoach;
    private String maDviDxuat;
    private String loaiHinhNx;
    private String kieuNx;
    private String soDxuat;
    private String trichYeu;
    private String loaiVthh;
    private String dvt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDxuat;
    private String trangThai;
    private BigDecimal tongSlNhap;
    private BigDecimal tongThanhTien;
    private List<FileDinhKemReq> fileDinhKems;
    private List<FileDinhKemReq> canCuPhapLy;
    private List<HhDxuatKhNhapKhacDtlReq> details;
}
