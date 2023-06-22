package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenCucKhacCucDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenNoiBoCucDtl;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ThKeHoachDieuChuyenCucHdrReq {
    private Long id;

    private String maTongHop;

    private String soDeXuat;

    private LocalDate ngayTongHop;

    private String trichYeu;

    private Integer namKeHoach;

    private String loaiDieuChuyen;

    private LocalDate thTuNgay;

    private LocalDate thDenNgay;

    private String trangThai;

    private LocalDate ngayGDuyet;

    private Long nguoiGDuyetId;

    private LocalDate ngayDuyetTp;

    private Long nguoiDuyetTPId;

    private LocalDate ngayDuyetLdc;

    private Long nguoiDuyetLdcId;

    private String lyDoTuChoi;

    private String maDVi;

    private String tenDVi;

    private LocalDateTime thoiGianTongHop;

    private LocalDate ngayTrinhDuyetTc;

    private LocalDate ngayPheDuyetTc;

    private String loaiHinhNhapXuat;

    private String tenLoaiHinhNhapXuat;

    private String kieuNhapXuat;

    private String tenKieuNhapXuat;
    private List<FileDinhKemReq> canCu = new ArrayList<>();
    List<THKeHoachDieuChuyenNoiBoCucDtl> thKeHoachDieuChuyenNoiBoCucDtls;

    List<THKeHoachDieuChuyenCucKhacCucDtl> thKeHoachDieuChuyenCucKhacCucDtls;
}
