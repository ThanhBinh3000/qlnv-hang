package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlBangKeReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maQhns;
    private String soBangKe;
    private LocalDate ngayLap;
    private String soQdXh;
    private Long idQdXh;
    private String ngayQdXh;
    private String soPhieuXuatKho;
    private Long idPhieuXuatKho;
    private String diaDiemKho;
    private String nguoiGiamSat;
    private String lyDoTuChoi;
    private String trangThai;
    private String loaiVthh;
    private String cloaiVthh;
    private BigDecimal tongTrongLuong;
    private List<XhTlBangKeDtl> children = new ArrayList<>();

}