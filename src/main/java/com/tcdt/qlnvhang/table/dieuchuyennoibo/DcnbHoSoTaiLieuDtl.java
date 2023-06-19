package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = DcnbHoSoTaiLieuDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbHoSoTaiLieuDtl extends BaseEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_HO_SO_TAI_LIEU_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbHoSoTaiLieuDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbHoSoTaiLieuDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbHoSoTaiLieuDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "DCNB_HO_SO_KY_THUAT_HDR_ID")
    private Long hoSoKyThuatHdrId;
    @Column(name = "TEN_HO_SO")
    private String tenHoSo;
    @Column(name = "LOAI_TAI_LIEU")
    private String loaiTaiLieu;
    @Column(name = "SO_LUONG")
    private String soLuong;
    @Column(name = "GHI_CHU")
    private String ghiChu;
    @Column(name = "FILE_DINH_KEM_ID")
    private Long fileDinhKemId;
    @Column(name = "THOI_GIAN_TAO")
    private LocalDate thoiGianTao;
    @Column(name = "THOI_DIEM_TAO")
    private LocalDate thoiDiemTao;
    @Column(name = "TEN_FILE_DINH_KEM")
    private String tenFileDinhKem;
}
