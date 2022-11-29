package com.tcdt.qlnvhang.table.khoahoccongnghebaoquan;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "KH_CN_NGHIEM_THU_THANH_LY_DTL")
@Data
public class KhCnNghiemThuThanhLyDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "KH_CN_NGHIEM_THU_THANH_LY_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KH_CN_NGHIEM_THU_DTL_SEQ")
    @SequenceGenerator(sequenceName = "KH_CN_NGHIEM_THU_DTL_SEQ", allocationSize = 1, name = "KH_CN_NGHIEM_THU_DTL_SEQ")

    private Long id;
    private Long idNghiemThu;
    private String hoTen;
    private String donVi;
}
