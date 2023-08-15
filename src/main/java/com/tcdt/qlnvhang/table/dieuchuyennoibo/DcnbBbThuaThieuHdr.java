package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBbThuaThieuHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbThuaThieuHdr extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_THUA_THIEU_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbThuaThieuHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbThuaThieuHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbThuaThieuHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maDviNhan;
    private String tenCanBo;
    private String canBoId;
    private String soBc;
    private LocalDate ngayLap;
    private Long qdDcCucId;
    private String soQdDcCuc;
    private LocalDate ngayKyQdCuc;
    private String soBcKetQuaDc;
    private String bcKetQuaDcId;
    private LocalDate ngayLapBcKetQuaDc;
    private String nguyenNhan;
    private String kienNghi;
    private String ghiChu;
    @Access(value = AccessType.PROPERTY)
    private String trangThai;

    @Transient
    private List<FileDinhKem> fileBienBanHaoDois = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBbThuaThieuDtl> chiTiet = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBbThuaThieuKiemKeDtl> banKiemKe = new ArrayList<>();
    @Transient
    private String tenTrangThai;

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
