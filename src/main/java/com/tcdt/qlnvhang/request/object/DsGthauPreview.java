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
    private String tenCloaiVthh;
    private String donGia;
    private String thanhTien;
    private String donGiaNhaThau;
    private String thanhTienNhaThau;
    private String chenhLech;
    private List<HhDthauNthauDuthau> dsNhaThau;
    private String nhaThauTrungThau;
}
