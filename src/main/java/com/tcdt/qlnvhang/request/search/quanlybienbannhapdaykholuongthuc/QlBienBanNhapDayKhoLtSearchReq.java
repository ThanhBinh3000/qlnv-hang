package com.tcdt.qlnvhang.request.search.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.search.BaseSearchRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class QlBienBanNhapDayKhoLtSearchReq extends BaseRequest {
    private String soBienBan;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDonViLap;
    private String maHang;
    private String maDonVi;
}
