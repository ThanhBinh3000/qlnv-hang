package com.tcdt.qlnvhang.request.search.xuathang;

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
public class XhQdGiaoNvuXuatSearchReq extends BaseRequest {
    private String soQuyetDinh;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKyTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKyDen;
    private Integer namXuat;
    private String trichyeu;
    private String loaiVthh;
}
