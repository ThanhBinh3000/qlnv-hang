package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class THKeHoachDieuChuyenCucKhacCucDtlReq {
    private LocalDate ngayGduyetTc;
    private Long hdrId;
    private Long dcnbKeHoachDcHdrId;
    private Long id;

    //
    private Long parentId;
    private String loaiDc;
    private String tenLoaiDc;
    private String type;
    private Integer nam;
    private String soDxuat;
    private LocalDate ngayLapKh;
    private LocalDate ngayDuyetLdcc;
    private Long nguoiDuyetLdccId;
    private String trichYeu;
    private String lyDoDc;
    private String maDvi;
    private String maDviPq;
    private String tenDvi;
    private String maCucNhan;
    private String tenCucNhan;
    private String trachNhiemDviTh;
    private String trangThai;
    private String lyDoTuChoi;
    private Long idThop;
    private String maThop;
    private Long idQdDc;
    private String soQdDc;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String maDviCuc;
    private String tenDviCuc;
    private BigDecimal tongDuToanKp;
    private Boolean daXdinhDiemNhap;
    private String tenTrangThai;

//    private List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdrs = new ArrayList<>();

    private List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = new ArrayList<>();
}
