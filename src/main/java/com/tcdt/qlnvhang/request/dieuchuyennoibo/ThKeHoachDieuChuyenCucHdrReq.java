package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.THKeHoachDieuChuyenCucKhacCucDtl;
import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.THKeHoachDieuChuyenNoiBoCucDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import com.tcdt.qlnvhang.util.Contains;
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

    private String maDVi;

    private String tenDVi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date thoiGianTongHop;

    List<THKeHoachDieuChuyenNoiBoCucDtl> thKeHoachDieuChuyenNoiBoCucDtls;

    List<THKeHoachDieuChuyenCucKhacCucDtl> thKeHoachDieuChuyenCucKhacCucDtls;
}
