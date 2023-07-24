package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbThuaThieuHdrReq extends BaseRequest {
    private Integer nam;
    private String maDvi;
    private String maDviNhan;
    private String canBoId;
    private String soBc;
    private LocalDate ngayLap;
    private Long qdDcCucId;
    private String soQdDcCuc;
    private LocalDate ngayKyQdCuc;
    private String soBcKetQuaDc;
    private String bcKetQuaDcId;
    private LocalDate ngayLapBcKetQuaDc;
    private String trangThai;
}
