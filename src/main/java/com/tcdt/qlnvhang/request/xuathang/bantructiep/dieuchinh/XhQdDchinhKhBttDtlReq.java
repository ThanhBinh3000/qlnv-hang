package com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdDchinhKhBttDtlReq  {

    private Long id;

    private Long idDcHdr;

    private Long idDxHdr;

    private String maDvi;

    private String diaChi;

    private String soDxuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;

    private String trichYeu;

    private String tenDuAn;

    private BigDecimal tongSoLuong;

    private BigDecimal tongTienVat;

    private BigDecimal giaChuaVat;

    private String thueGtgt;

    private String giaVat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienDen;

    private String thongBaoKh;

    private BigDecimal tongMucDauTu;

    private String nguonVon;

    @Transient
    List<XhQdDchinhKhBttSlReq> children = new ArrayList<>();


}
