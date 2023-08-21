package com.tcdt.qlnvhang.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import com.tcdt.qlnvhang.table.HhQdPheduyetKqMttSLDD;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBcanKeHangHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanDayKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhangDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class SearchHopDongMttHdrDTO extends BaseRequest {
    private Integer namKh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayCgiaTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayCgiaDen;
    private String maDvi;
    private String trangThaiQd;
    private String trangThaiKq;
    private String loaiVthh;
    private Integer namNhap;
    private String soQd;
    private String cloaiVthh;
    private String trichyeu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayQdTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayQdDen;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    String tuNgayKy;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    String denNgayKy;
    private String loaiQd;
    private String trichYeu;
    private Integer lastest;
}
