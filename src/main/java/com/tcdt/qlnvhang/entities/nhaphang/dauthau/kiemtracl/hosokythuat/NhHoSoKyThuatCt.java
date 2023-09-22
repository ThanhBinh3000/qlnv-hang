package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NH_HO_SO_KY_THUAT_CT")
@EqualsAndHashCode(callSuper = false)
public class NhHoSoKyThuatCt implements Serializable {
    private static final long serialVersionUID = 2660274262212943813L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HO_SO_KY_THUAT_CT_SEQ")
    @SequenceGenerator(sequenceName = "HO_SO_KY_THUAT_CT_SEQ", allocationSize = 1, name = "HO_SO_KY_THUAT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HO_SO_KY_THUAT_ID")
    private Long hoSoKyThuatId;

    @Column(name = "TEN_HO_SO")
    private String tenHoSo;

    @Column(name = "LOAI_TAI_LIEU")
    private String loaiTaiLieu;

    @Column(name = "SO_LUONG")
    private Integer soLuong;

    @Column(name = "GHI_CHU")
    private String ghiChu;
    @Transient
    private String fileName;

    @Transient
    private List<FileDinhKem> fileDinhKem;

}
