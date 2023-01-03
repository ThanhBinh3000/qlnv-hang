package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.kehoachbdg.dexuat;

import com.tcdt.qlnvhang.contraints.CompareDate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDxKhBanDauGiaPhanLoReq {
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal duDau;
    private BigDecimal soLuong;
    private BigDecimal donGiaDeXuat;
    private BigDecimal donGiaVat;
    private String dviTinh;


}
