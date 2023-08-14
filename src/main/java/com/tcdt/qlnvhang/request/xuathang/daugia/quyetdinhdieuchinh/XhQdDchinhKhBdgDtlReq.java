package com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBdgDtlReq {
    private Long id;
    private Long idHdr;
    private Long idQdGoc;
    private String maDvi;
    private String diaChi;
    private String soDxuat;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String pthucGnhan;
    private String thongBaoKh;
    private BigDecimal khoanTienDatTruoc;
    private String loaiVthh;
    private String cloaiVthh;
    private List<XhQdDchinhKhBdgPlReq> children = new ArrayList<>();
}
