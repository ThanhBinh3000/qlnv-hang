package com.tcdt.qlnvhang.response.quyetdinhpheduyetketqualuachonnhathauvatu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class QdKqlcntGoiThauVtRes {
    private Long id;
    private String tenGoiThau;
    private BigDecimal soLuong;
    private BigDecimal giaGoiThau;
    private String trangThai;
    private String tenDonViTrungThau;
    private Long loaiHopDongId;
    private String thoiGianThucHien;
    private BigDecimal donGiaTruocThue;
    private BigDecimal thueVat;
    private BigDecimal donGiaSauThue;
    private BigDecimal donGiaHopDongTruocThue;
    private BigDecimal donGiaHopDongSauThue;
    private Long qdPdKhlcntId;
    private Long vatTuId;
    private String maVatTu;
    private String tenVatTu;

    private List<QdKqlcntGtDdnVtRes> ddns = new ArrayList<>();
}
