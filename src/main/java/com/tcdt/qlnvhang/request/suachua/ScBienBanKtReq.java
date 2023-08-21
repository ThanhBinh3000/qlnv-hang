package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBienBanKtDd;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBienBanKtDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScBienBanKtReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maQhns;
    private String soBienBan;
    private LocalDate ngayLap;
    private String soQdNh;
    private Long idQdNh;
    private Long idScDanhSachHdr;
    private String maDiaDiem;
    private String loaiVthh;
    private String cloaiVthh;
    private String lyDoTuChoi;
    private String trangThai;
    private String ghiChu;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private BigDecimal tongSoLuong;
    private List<ScBienBanKtDtl> children = new ArrayList<>();
    private List<ScBienBanKtDd> daiDienList = new ArrayList<>();

    private List<FileDinhKemReq> fileCanCuReq = new ArrayList<>();

    //
    private String maDviSr;
    private LocalDate ngayTu;
    private LocalDate ngayDen;
    private LocalDate ngayNhapTu;
    private LocalDate ngayNhapDen;
}
