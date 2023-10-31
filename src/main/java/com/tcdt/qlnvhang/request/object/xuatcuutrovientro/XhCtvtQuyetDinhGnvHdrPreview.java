package com.tcdt.qlnvhang.request.object.xuatcuutrovientro;

import com.tcdt.qlnvhang.response.xuatcuutrovientro.XhCtvtQuyetDinhGnvDtlDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class XhCtvtQuyetDinhGnvHdrPreview {
    private String tenDonvi;
    private String soBbQd;
    private int ngayKy;
    private int thangKy;
    private int namKy;
    private int namKeHoach;
    private String canCuPhapLy;
    private BigDecimal tongSoLuong;
    private String donViTinh;
    private String loaiVthh;
    private String thoiGianGiaoNhan;
    private String lanhDaoCuc;
    private List<XhCtvtQuyetDinhGnvDtlDto> xhCtvtQuyetDinhGnvDtlDto;
}
