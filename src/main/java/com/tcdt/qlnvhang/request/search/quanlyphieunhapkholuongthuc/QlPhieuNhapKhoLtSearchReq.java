package com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.search.BaseSearchRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QlPhieuNhapKhoLtSearchReq extends BaseRequest {

    private Long soPhieu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayNhapKho;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayTaoPhieu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayGiaoNhanHang;
    private String maDiemKho;
    private String maNhaKho;
    private String nguoiGiaoHang;
    private Long vatTuId;
    private String maDonVi;
    private String maKhoNgan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tuNgay;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate denNgay;
}
