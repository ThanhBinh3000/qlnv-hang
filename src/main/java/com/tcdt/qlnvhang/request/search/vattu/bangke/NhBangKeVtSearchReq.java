package com.tcdt.qlnvhang.request.search.vattu.bangke;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NhBangKeVtSearchReq extends BaseRequest {
    private String soBangKe;
    private String soQdNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayTaoBangKeTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayTaoBangKeDen;
    private String loaiVthh;
}
