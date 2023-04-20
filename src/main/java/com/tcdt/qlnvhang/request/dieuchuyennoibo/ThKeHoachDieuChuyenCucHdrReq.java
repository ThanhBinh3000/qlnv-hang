package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.THKeHoachDieuChuyenNoiBoCucDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import lombok.Data;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ThKeHoachDieuChuyenCucHdrReq {
    private Long id;

    private LocalDate ngaytao;

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

    private Long nguoiDuyetTPId;

    private LocalDate ngayDuyetLdc;

    private Long nguoiDuyetLdcId;

    private String lyDoTuChoi;

    private String maDonVi;

    private String tenDonVi;

    private LocalDate thoiGianTongHop;


    List<ThKeHoachDieuChuyenNoiBoCucDtlReq> ctTongHopKeHoachDieuChuyen;


    List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdrs = new ArrayList<>();;


    List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtlList = new ArrayList<>();;
}
