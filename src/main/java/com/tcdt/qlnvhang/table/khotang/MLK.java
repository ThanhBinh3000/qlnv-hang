package com.tcdt.qlnvhang.table.khotang;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class MLK {
    Long id;
    String maDtqgkv;
    String tenDtqgkv;
    String diaChi;
    Long tinhthanhId;
    Long quanhuyenId;
    Long phuongxaId;
    String moTa;
    Long quyhoachDuyetId;
    Long dtqgkvHientrangId;
    BigDecimal tichLuongKhaDung;
    BigDecimal tichLuongChua;
    BigDecimal tichLuongChuaLt;
    BigDecimal tichLuongChuaVt;
    BigDecimal tichLuongKdLt;
    BigDecimal tichLuongKdVt;
    BigDecimal tichLuongThietKe;
    BigDecimal tichLuongKdLtvt;
    BigDecimal tichLuongChuaLtGao;
    BigDecimal tichLuongChuaLtThoc;
    Long updateStatus;
    Date lastUpdate;
    String trangThai;
    Date ngayTao;
    String nguoiTao;
    Date ngaySua;
    String nguoiSua;
    String ldoTuchoi;
    Date ngayGuiDuyet;
    String nguoiGuiDuyet;
    Date ngayPduyet;
    String nguoiPduyet;
    Date ngayDongboSauCung;

    private List<KtDtqgkv> child;
}
