package com.tcdt.qlnvhang.request.search.xuathang;

import com.tcdt.qlnvhang.request.PaggingReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhBienBanHaoDoiSearchReq {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_LIMIT = 10;

    public static final String ORDER_BY = "id";

    public static final String ORDER_TYPE = "asc";
    private PaggingReq paggingReq;
    private String soQuyetDinh;
    private String soBienBan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayBienBanTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayBienBanDen;
    public PaggingReq getPaggingReq() {
        if (this.paggingReq == null) {
            this.paggingReq = new PaggingReq(DEFAULT_LIMIT, DEFAULT_PAGE,ORDER_BY,ORDER_TYPE);
        }
        return this.paggingReq;
    }
}
