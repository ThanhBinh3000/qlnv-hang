package com.tcdt.qlnvhang.request.search.xuathang;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class XhQdGiaoNvuXuatSearchReq extends BaseRequest {
    private Integer nam;
    private String soQd;
    private String loaiVthh;
    private String trichYeu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoDen;

    private String trangThai;
    private String maDvi;
}
