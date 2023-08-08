package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeXuatVatTuDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
public class ScBangKeXuatVatTuReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maQhns;
    private String soBangKe;
    private LocalDate ngayNhap;
    private LocalDate ngayXuatKho;
    private String soQdXh;
    private Long idQdXh;
    private String soPhieuXuatKho;
    private Long idPhieuXuatKho;
    private String lyDoTuChoi;
    private Long idScDanhSachHdr;
    List<ScBangKeXuatVatTuDtl> children = new ArrayList<>();

    //Region search
    private LocalDate ngayTuXh;
    private LocalDate ngayDenXh;
    private LocalDate ngayTu;
    private LocalDate ngayDen;
    private String maDviSr;


}
