package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbBbKqDcSearch extends BaseRequest {
    private String maDvi;
    private String soBc;
    private String trangThai;
    private String soQdinhCuc;
    private LocalDate tuNgay;
    private LocalDate denNgay;
}
