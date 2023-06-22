package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ScTrinhVaThamDinhReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String soHoSo;
    private LocalDate ngayDuyetTu;
    private LocalDate ngayDuyetDen;
    private String soToTrinh;
    private String ngayTao;
    private String maDanhSachSc;
    private LocalDate ngayDuyet;
    private LocalDate thoiHanXuatScDuKien;
    private LocalDate thoiHanNhapScDuKien;
    private String soQdSc;
    private List<FileDinhKemReq> canCu;
    private List<FileDinhKemReq> fileDinhKem;
    private List<ScDanhSachHdr> scDanhSachHdrs;

}
