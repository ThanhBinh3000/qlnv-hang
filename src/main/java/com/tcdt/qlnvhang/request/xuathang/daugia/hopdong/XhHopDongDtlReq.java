package com.tcdt.qlnvhang.request.xuathang.daugia.hopdong;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhHopDongDtlReq {
    private Long id;

    private Long idHdr;

    private String maDvi;

    private BigDecimal soLuong;

    private BigDecimal donGiaVat;

    private String diaChi;


    //    phu luc
    private Long idHdDtl;

    private List<XhDdiemNhapKhoReq> children = new ArrayList<>();

}
