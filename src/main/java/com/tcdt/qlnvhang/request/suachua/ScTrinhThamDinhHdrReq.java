package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import lombok.Data;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScTrinhThamDinhHdrReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String soTtr;
    private Long idThHdr;
    private String maThHdr;
    private LocalDate ngayDuyetLdc;
    private LocalDate thoiHanNhapDk;
    private LocalDate thoiHanNhap;
    private LocalDate thoiHanXuatDk;
    private LocalDate thoiHanXuat;
    private String trichYeu;
    private String ysKien;
    private String lyDoTuChoi;
    private String trangThai;
    private String ketQua;
    private List<FileDinhKemReq> fileDinhKemReq;
    private List<FileDinhKemReq> fileCanCuReq;
    private List<ScTrinhThamDinhDtlReq> children;

    // Region Sr
    private LocalDate ngayTuCuc;
    private LocalDate ngayTuTc;
    private LocalDate ngayDenCuc;
    private LocalDate ngayDenTc;
    private String soQdScSr;

}
