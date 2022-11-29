package com.tcdt.qlnvhang.table.khoahoccongnghebaoquan;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "KH_CN_NGHIEM_THU_THANH_LY")
@Data
public class KhCnNghiemThuThanhLy implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "KH_CN_NGHIEM_THU_THANH_LY";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KH_CN_NGHIEM_THU_THANH_LY_SEQ")
    @SequenceGenerator(sequenceName = "KH_CN_NGHIEM_THU_THANH_LY_SEQ", allocationSize = 1, name = "KH_CN_NGHIEM_THU_THANH_LY_SEQ")

    private Long id;
    private Long idHdr;
    private String hoTen;
    private String donVi;
}
