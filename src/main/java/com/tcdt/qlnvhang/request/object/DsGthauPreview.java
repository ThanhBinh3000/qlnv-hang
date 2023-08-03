package com.tcdt.qlnvhang.request.object;

import com.tcdt.qlnvhang.table.HhDthauNthauDuthau;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class DsGthauPreview {
    private String goiThau;
    private BigDecimal soLuong;
    private String dvt;
    private List<HhDthauNthauDuthau> dsNhaThau;
    private String nhaThauTrungThau;
}
