package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private Date ngaytao;

    private Long nguoiTaoId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngaySua;

    private Long nguoiSuaId;

    private String maTongHop;

    private String soDeXuat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTongHop;

    private String trichYeu;

    private Integer namKeHoach;

    private String loaiDieuChuyen;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thTuNgay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thDenNgay;

    private String trangThai;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGDuyet;

    private Long nguoiGDuyetId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDuyetTp;

    private Long nguoiDuyetTPId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDuyetLdc;

    private Long nguoiDuyetLdcId;

    private String lyDoTuChoi;

    private String maDVi;

    private String tenDVi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thoiGianTongHop;

    private Boolean daXdinhDiemNhap;

    List<ThKeHoachDieuChuyenNoiBoCucDtlReq> ctTongHopKeHoachDieuChuyen;

    List<ThKeHoachDieuChuyenKhacCucDtlReq> ctTongHopKeHoachDieuChuyenKhacCuc;
}
