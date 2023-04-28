package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Data
public class ThKeHoachDieuChuyenNoiBoCucDtlReq {
    private Long id;

    private Long hdrId;;

    private Long dcKeHoachDcHdrId;

    private Long dcKeHoachDcDtlId;

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

    List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = new ArrayList<>();
}
