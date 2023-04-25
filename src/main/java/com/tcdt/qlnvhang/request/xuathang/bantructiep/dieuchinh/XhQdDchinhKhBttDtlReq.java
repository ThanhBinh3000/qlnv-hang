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

    private Long idQdGoc;

    private String maDvi;

    private String diaChi;

    private String soDxuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;

    private String trichYeu;

    private String tenDuAn;

    private BigDecimal tongSoLuong;

    private BigDecimal donGiaVat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienDen;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Integer tgianTtoan;

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;

    @Transient
    List<XhQdDchinhKhBttSlReq> children = new ArrayList<>();


}
