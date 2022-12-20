package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongDdiemNhapKhoVt;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "HH_DIA_DIEM_GIAO_NHAN_HANG")
@Data
public class HhDiaDiemGiaoNhanHang implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DIA_DIEM_GIAO_NHAN_HANG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DIA_DIEM_GIAO_NHAN_HANG_SEQ")
    @SequenceGenerator(sequenceName = "HH_DIA_DIEM_GIAO_NHAN_HANG_SEQ", allocationSize = 1, name = "HH_DIA_DIEM_GIAO_NHAN_HANG_SEQ")
    private Long id;
    String type;
    Long idHdongDtl;

    String maDvi;
    String maDiemKho;
    BigDecimal soLuong;
    BigDecimal donGiaVat;
    String dviTinh;
    String trangThai;
    @Transient
    String tenTrangThai;
    @Transient
    String tenDvi;
    @Transient
    String tenDiemKho;


    public String getTenTrangThai() {
        return TrangThaiAllEnum.getLabelById(trangThai);
    }
}


