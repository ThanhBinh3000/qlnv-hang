package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class XhTcTtinBttReq {
    private Long id;
    private Long idDviDtl;
    private Long idQdPdDtl;
    private String tochucCanhan;
    private String mst;
    private String diaDiemChaoGia;
    private String sdt;
    private LocalDate ngayChaoGia;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private Boolean luaChon;
    @Transient
    private FileDinhKemReq fileDinhKems;
}
