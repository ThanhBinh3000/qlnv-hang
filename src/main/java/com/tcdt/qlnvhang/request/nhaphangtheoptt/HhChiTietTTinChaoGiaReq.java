package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
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
