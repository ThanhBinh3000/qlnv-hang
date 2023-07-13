package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHangDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScQuyetDinhNhapHangReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String soQdNhapHang;
    private LocalDate ngayKyQd;
    private String ketQuaKiemDinh;
    private LocalDate ngayKiemDinh;
    private Long soQdGiaoNvXhId;
    private String soQdGiaoNvXh;
    private LocalDate thoiHanXuatSc;
    private LocalDate thoiHanNhapSc;
    private String duToanKinhPhi;
    private String loaiHinhNhapXuat;
    private String kieuNhapXuat;
    private String trichYeu;

    private List<FileDinhKemReq> fileCanCuReq;
    private List<FileDinhKemReq> fileDinhKemReq;

    private List<ScQuyetDinhNhapHangDtl> children = new ArrayList<>();


}
