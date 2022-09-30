package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhChiTietTTinChaoGiaReq {
    private Long id;

    private String canhanTochuc;
    private String mst;
    private String diaChi;
    private String sdt;
    @Temporal(TemporalType.DATE)
    private Date ngayChaoGia;
    private BigDecimal soLuong;
    private BigDecimal dgiaChuaThue;
    private BigDecimal thueGtgt;
    private BigDecimal thanhTien;
    private Integer luaChon;
    private Integer luaChonPduyet;
    private Long idSoQdPduyetCgia;

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();
}
