package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class HhChiTietTTinChaoGiaReq {
    private Long id;

    private Long idQdPdSldd;

    private String canhanTochuc;

    private String mst;

    private String diaChi;

    private String sdt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayChaoGia;

    private BigDecimal soLuong;

    private BigDecimal donGia;
    private BigDecimal donGiaVat;

    private BigDecimal thueGtgt;

    private BigDecimal thanhTien;

    private Boolean luaChon;

    @Transient
    private FileDinhKemReq fileDinhKems;
}
