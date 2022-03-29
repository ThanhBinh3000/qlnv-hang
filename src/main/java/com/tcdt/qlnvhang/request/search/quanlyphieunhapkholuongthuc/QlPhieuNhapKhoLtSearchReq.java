package com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.request.search.BaseSearchRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QlPhieuNhapKhoLtSearchReq extends BaseSearchRequest {

    private String soPhieu;
    private Long vatTuId;
    private String maDonVi;
    private String maKhoNgan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tuNgay;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate denNgay;
}
