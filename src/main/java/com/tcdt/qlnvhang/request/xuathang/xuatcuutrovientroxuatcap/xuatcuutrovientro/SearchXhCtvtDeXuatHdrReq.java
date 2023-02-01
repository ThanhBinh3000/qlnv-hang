package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;
@Data
public class SearchXhCtvtDeXuatHdrReq extends BaseRequest {
    private Integer nam;
    private String maDvi;
    private String dvql;
    private String soDx;
    private LocalDate ngayDxTu;
    private LocalDate ngayDxDen;
    private LocalDate ngayKetThucTu;
    private LocalDate ngayKetThucDen;
    private String trangThai;
    private String type;

}
