package com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QlPhieuNhapKhoLtSearchReq extends BaseRequest {

    private Long soPhieu;
    private String maDvi;
    private String soQdNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tuNgayNhapKho;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate denNgayNhapKho;


}
