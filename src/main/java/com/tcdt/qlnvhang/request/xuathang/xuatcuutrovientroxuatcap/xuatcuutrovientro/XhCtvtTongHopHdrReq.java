package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class XhCtvtTongHopHdrReq{
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maTongHop;
    private LocalDate ngayThop;
    private String noiDungThop;
    private String loaiVthh;
    private String cloaiVthh;
    private String trangThai;
    private Long idQdPd;
    private String soQdPd;
    private LocalDate ngayKyQd;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private String type;
    private String loaiNhapXuat;

    private BigDecimal tongSlCtVt;
    private BigDecimal tongSlXuatCap;

}
