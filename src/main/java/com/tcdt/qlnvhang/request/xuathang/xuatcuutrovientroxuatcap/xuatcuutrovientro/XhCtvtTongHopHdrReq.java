package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
    private String tenVthh;
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
    private BigDecimal tongSlCtvt;
    private BigDecimal tongSlXuatCap;
    private BigDecimal tongSlDeXuat;
    private List<XhCtvtTongHopDtl> deXuatCuuTro = new ArrayList<>();

}
