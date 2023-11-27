package com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.nhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan.NhBbGiaoNhanVtCt;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class NhBbGiaoNhanVtPreview {
    private String tenDvi;
    private String tenCuc;
    private String tenCucUpper;
    private String tenChiCucUpper;
    private String tenChiCuc;
    private String tenDiemKho;
    private String soHd;
    private String ngayKy;
    private String tenNhaThau;
    private String tenNhaThauUpper;
    private BigDecimal soLuong;
    private String ngayTao;
    private String thangTao;
    private String namTao;
    private String tenCloaiVthh;
    private String tenCloaiVthhUpper;
    private List<FileDinhKem> fileDinhKems;
    private List<NhBbGiaoNhanVtCt> dsDaiDienCuc;
    private List<NhBbGiaoNhanVtCt> dsDaiDienChiCuc;
    private List<NhBbGiaoNhanVtCt> dsDaiDienNhaThau;
}
