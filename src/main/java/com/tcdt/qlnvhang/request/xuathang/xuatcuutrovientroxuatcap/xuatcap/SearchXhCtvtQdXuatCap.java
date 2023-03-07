package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchXhCtvtQdXuatCap extends BaseRequest {
    private Integer nam;
    private String soQdXc;
    private LocalDate ngayXuatCtvtTu;
    private LocalDate ngayXuatCtvtDen;
    private LocalDate ngayHieuLucTu;
    private LocalDate ngayHieuLucDen;
}
