package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchXhCtvtTongHopHdr extends BaseRequest {
    private Integer nam;
    private String maDviDx;
    private String dvql;
    private String soDx;
    private LocalDate ngayDxTu;
    private LocalDate ngayDxDen;
    private LocalDate ngayKetThucDxTu;
    private LocalDate ngayKetThucDxDen;
    private String trangThai;
    private String type;

}
