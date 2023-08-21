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
    private String soQd;
    private LocalDate ngayKy;
    private String soPhieuKtcl;
    private String idPhieuKtcl;
    private LocalDate ngayKiemDinh;
    private Long idQdXh;
    private String soQdXh;
    private LocalDate thoiHanXuat;
    private LocalDate thoiHanNhap;
    private String duToanChiPhi;
    private String loaiHinhNhapXuat;
    private String kieuNhapXuat;
    private String trichYeu;
    private String trangThai;
    private List<FileDinhKemReq> fileCanCuReq;
    private List<FileDinhKemReq> fileDinhKemReq;

    private List<ScQuyetDinhNhapHangDtl> children = new ArrayList<>();

    private String maDviSr;

    // Search
    private LocalDate thoiHanNhapTu;
    private LocalDate thoiHanNhapDen;

}
