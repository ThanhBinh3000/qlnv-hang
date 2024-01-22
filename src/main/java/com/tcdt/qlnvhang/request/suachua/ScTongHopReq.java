package com.tcdt.qlnvhang.request.suachua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ScTongHopReq extends BaseRequest {

    // #Region Entity
    private Long id;
    private Integer nam;
    private String maDanhSach;
    private String tenDanhSach;
    private LocalDate thoiHanXuat;
    private LocalDate thoiHanNhap;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date thoiGianTh;

    // #Region Search
    private String dvql;
    private String maDviSr;
    private int namSr;

    private LocalDate ngayTu;

    private LocalDate ngayDen;

    private String maDanhSachSr;

}
