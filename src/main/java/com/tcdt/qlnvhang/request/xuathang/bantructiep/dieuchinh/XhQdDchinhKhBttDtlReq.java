package com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBttDtlReq {
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String diaChi;
    private Long idDxHdr;
    private String soDxuat;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private String donViTinh;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String pthucGnhan;
    private String thongBao;
    List<XhQdDchinhKhBttSlReq> children = new ArrayList<>();
}
