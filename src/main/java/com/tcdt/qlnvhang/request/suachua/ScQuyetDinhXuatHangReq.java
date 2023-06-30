package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ScQuyetDinhXuatHangReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String soQd;
    private LocalDate ngayKyQd;
    private Long qdScTcId;
    private String qdScTc;
    private LocalDate ngayKyQdScTc;
    private LocalDate thoiHanXuat;
    private LocalDate thoiHanNhap;
    private BigDecimal duToanKinhPhi;
    private String loaiHinhNhapXuat;
    private String kieuNhapXuat;
    private String trichYeu;
    private List<FileDinhKemReq> canCu;
    private List<FileDinhKemReq> fileDinhKem;
}
