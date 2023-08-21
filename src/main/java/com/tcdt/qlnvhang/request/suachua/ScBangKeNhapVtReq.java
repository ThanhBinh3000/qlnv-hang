package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScBangKeNhapVtReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maQhns;
    private String soBangKe;
    private LocalDate ngayNhap;
    private String soQdNh;
    private Long idQdNh;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private Long idPhieuNhapKho;
    private Long idThuKho;
    private Long idLanhDaoCc;
    private String lyDoTuChoi;
    private String trangThai;
    private List<ScBangKeNhapVtDtl> children = new ArrayList<>();

    //Param search

    private LocalDate ngayTu;
    private LocalDate ngayDen;
    private LocalDate ngayTuNh;
    private LocalDate ngayDenNh;
    private List<Long> idPhieuNhapKhoList;
}
