package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenCucKhacCucDtl;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ThKeHoachDieuChuyenTongCucDtlReq {
    private Long id;

    private Long hdrId;

    private Long thKhDcHdrId;

    private Long thKhDcDtlId;

    private Long keHoachDcHdrId;

    private Long keHoachDcDtlId;

    private Long tongDuToanKp;

    //Trường Tổng hợp kế hoạch điều chuyển
    private LocalDate ngayTao;

    private Long nguoiTaoId;

    private LocalDate ngaySua;

    private Long nguoiSuaId;

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

    private Long nguoiDuyetTpId;

    private LocalDate ngayDuyetLdc;

    private Long nguoiDuyetLdcId;

    private String lyDoTuChoi;

    private String maDvi;

    private String tenDvi;

    private String maCucNhan;

    private String tenCucNhan;

    private LocalDateTime thoiGianTongHop;

    List<THKeHoachDieuChuyenCucKhacCucDtl> thKeHoachDieuChuyenCucKhacCucDtls = new ArrayList<>();

}
