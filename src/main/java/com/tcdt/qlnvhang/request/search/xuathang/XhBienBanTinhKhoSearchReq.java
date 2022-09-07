package com.tcdt.qlnvhang.request.search.xuathang;

import com.tcdt.qlnvhang.request.PaggingReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class XhBienBanTinhKhoSearchReq{
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_LIMIT = 10;

    public static final String ORDER_BY = "id";

    public static final String ORDER_TYPE = "asc";
    private PaggingReq paggingReq;
    private String quyetDinhId;
    private String soBienBan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayXuatTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayXuatDen;
    public PaggingReq getPaggingReq() {
        if (this.paggingReq == null) {
            this.paggingReq = new PaggingReq(DEFAULT_LIMIT, DEFAULT_PAGE,ORDER_BY,ORDER_TYPE);
        }
        return this.paggingReq;
    }
}
