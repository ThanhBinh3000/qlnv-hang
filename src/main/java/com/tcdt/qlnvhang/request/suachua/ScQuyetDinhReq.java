package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class ScQuyetDinhReq extends BaseRequest {
    private Long id;
    private String soQd;
    private String trichYeu;
    private Long idToTrinh;
    private String soToTrinh;
    @DateTimeFormat(pattern = "dd-MM-yyyy ")
    private LocalDate ngayKy;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate ngayDuyetToTrinh;
    private LocalDate thoiHanXuatSc;
    private LocalDate thoiHanNhapSc;
    private List<FileDinhKemReq> fileDinhKem;
    private List<FileDinhKemReq> canCu;
    private LocalDate ngayKyTu;
    private LocalDate ngayKyDen;

}
