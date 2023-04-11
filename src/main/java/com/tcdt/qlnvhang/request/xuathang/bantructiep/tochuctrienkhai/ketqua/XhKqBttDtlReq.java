package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDdiem;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhKqBttDtlReq {

    private Long id;

    private Long idHdr;

    private String maDvi;
    @Transient
    private String tenDvi;

    private BigDecimal soLuongChiCuc;

    private String diaChi;

    private BigDecimal donGiaVat;

    @Transient
    private List<XhKqBttDdiemReq> children = new ArrayList<>();

}
