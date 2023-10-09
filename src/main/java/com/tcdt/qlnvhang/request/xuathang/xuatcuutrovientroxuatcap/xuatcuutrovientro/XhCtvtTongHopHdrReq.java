package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdr;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link XhCtvtTongHopHdr} entity
 */
@Data
public class XhCtvtTongHopHdrReq{
    private final LocalDateTime ngayTao;
    private final Long nguoiTaoId;
    private final LocalDateTime ngaySua;
    private final Long nguoiSuaId;
    private final Long id;
    private final Integer nam;
    private final String maDvi;
    private final String maTongHop;
    private final LocalDate ngayThop;
    private final String noiDungThop;
    private final String loaiNhapXuat;
    private final String kieuNhapXuat;
    private final String loaiVthh;
    private final String cloaiVthh;
    private final String tenVthh;
    private final String trangThai;
    private final Long idQdPd;
    private final String soQdPd;
    private final LocalDate ngayKyQd;
    private final LocalDate ngayGduyet;
    private final Long nguoiGduyetId;
    private final LocalDate ngayPduyet;
    private final Long nguoiPduyetId;
    private final String lyDoTuChoi;
    private final BigDecimal tongSlCtVt;
    private final BigDecimal tongSlXuatCap;
    private final BigDecimal tongSlDeXuat;
    private final List<XhCtvtTongHopDtl> deXuatCuuTro;
}
