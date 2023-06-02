package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenCucKhacCucDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenNoiBoCucDtl;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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

    List<THKeHoachDieuChuyenNoiBoCucDtl> thKeHoachDieuChuyenNoiBoCucDtls;

    List<THKeHoachDieuChuyenCucKhacCucDtl> thKeHoachDieuChuyenCucKhacCucDtls;
}
