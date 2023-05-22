package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class XhTcTtinBttReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idDtl;

    private Long idDviDtl;

    private String tochucCanhan;

    private String mst;

    private String diaDiemChaoGia;

    private String sdt;

    private LocalDate ngayChaoGia;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal thueGtgt;

    private Boolean luaChon;

    @Transient
    private FileDinhKemReq fileDinhKems;

}
