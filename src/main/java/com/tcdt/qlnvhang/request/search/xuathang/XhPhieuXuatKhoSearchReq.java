package com.tcdt.qlnvhang.request.search.xuathang;

import com.tcdt.qlnvhang.request.PaggingReq;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class XhPhieuXuatKhoSearchReq {
    private String soQuyetDinh;
    private String soPhieu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tuNgay;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate denNgay;
    private String soHD;

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_LIMIT = 10;
    public static final String ORDER_BY = "id";

    public static final String ORDER_TYPE = "asc";
    PaggingReq paggingReq;
    public PaggingReq getPaggingReq() {
        if (this.paggingReq == null) {
            this.paggingReq = new PaggingReq(DEFAULT_LIMIT, DEFAULT_PAGE,ORDER_BY,ORDER_TYPE);
        }
        return this.paggingReq;
    }
}
