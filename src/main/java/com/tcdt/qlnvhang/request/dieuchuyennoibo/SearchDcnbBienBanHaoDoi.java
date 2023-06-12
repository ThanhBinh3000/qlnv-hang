package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SearchDcnbBienBanHaoDoi extends BaseRequest {
    private Integer nam;
    private String soQdinhDcc;
    private String soBbHaoDoi;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDvi;
}
