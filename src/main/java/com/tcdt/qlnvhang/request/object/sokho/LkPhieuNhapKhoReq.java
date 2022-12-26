package com.tcdt.qlnvhang.request.object.sokho;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;

@Data
public class LkPhieuNhapKhoReq extends BaseRequest {
    String maDvi;

    Integer nam;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date tuNgay;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date denNgay;

    String loaiVthh;

    String cloaiVthh;

    String maDiemKho;

    String maNhaKho;

    String maNganKho;

    String maLoKho;

    String trangThai;
}
