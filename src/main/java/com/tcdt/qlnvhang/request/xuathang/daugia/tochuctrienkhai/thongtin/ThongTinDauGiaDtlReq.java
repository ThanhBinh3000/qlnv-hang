package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ThongTinDauGiaDtlReq extends BaseRequest {
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String diaChi;
    private BigDecimal soLuongXuatBan;
    private BigDecimal tienDatTruoc;

    public void setSoLuongChiCuc(BigDecimal soLuongXuatBan) {
        this.soLuongXuatBan = soLuongXuatBan;
    }

    private List<ThongTinDauGiaPloReq> children = new ArrayList<>();
}
