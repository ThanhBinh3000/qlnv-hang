package com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.search.BaseSearchRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QlBangKeCanHangLtSearchReq extends BaseRequest {
    private String soBangKe;
    private String soQdNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tuNgayNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate denNgayNhap;
    private String maVatTuCha;
}
