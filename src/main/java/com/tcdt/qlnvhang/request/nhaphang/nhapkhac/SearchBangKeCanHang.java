package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchBangKeCanHang extends BaseRequest {
    private Integer nam;
    private String loaiDc;
    private String loaiQdinh;
    private String soQdinhDcc;
    private String soBangKe;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maLoKho;
    private String maNganKho;
    private String maDvi;
    private String type;
    private String typeDataLink;
    private Boolean thayDoiThuKho;
    private List<String> dsLoaiHang = new ArrayList<>();
}
