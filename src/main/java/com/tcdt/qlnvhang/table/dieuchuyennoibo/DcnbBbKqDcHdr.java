package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBbKqDcHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbKqDcHdr extends BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_KQ_DC_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbKqDcHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbKqDcHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbKqDcHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maDviNhan;
    private String soBc;
    private LocalDate ngayBc;
    private String tenBc;
    private Long qdDcCucId;
    private String soQdDcCuc;
    private Long qdDcTcId;
    private String soQdDcTc;
    private LocalDate ngayKyQdTc;
    private String noiDung;
    @Access(value = AccessType.PROPERTY)
    private String trangThai;
    private String lyDoTuChoi;
    private LocalDate ngayGDuyet;
    private Long nguoiGDuyet;
    private LocalDate ngayDuyetTp;
    private Long nguoiDuyetTp;
    private LocalDate ngayPDuyet;
    private Long nguoiPDuyet;


    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBbKqDcDtl> danhSachDaiDien = new ArrayList<>();
    @Transient
    private String tenTrangThai;

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
