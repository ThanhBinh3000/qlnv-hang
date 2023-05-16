package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = DcnbHoSoBienBanDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbHoSoBienBanDtl extends BaseEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_HO_SO_BIEN_BAN_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbHoSoBienBanDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbHoSoBienBanDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbHoSoBienBanDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "DCNB_HO_SO_KY_THUAT_HDR_ID")
    private Long hoSoKyThuatHdrId;
    @Column(name = "TEN_BIEN_BAN")
    private String tenBienBan;
    @Column(name = "FILE_DINH_KEM_ID")
    private Long fileDinhKemId;
    @Column(name = "THOI_GIAN_TAO")
    private LocalDate thoiGianTao;
    @Column(name = "THOI_DIEM_TAO")
    private LocalDate thoiDiemTao;
    @Column(name = "TEN_FILE_DINH_KEM")
    private String tenFileDinhKem;
}
