package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_PHU_LUC_HOP_DONG_MTT")
@Data
public class HhPhuLucHopDongMtt implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_PHU_LUC_HOP_DONG_MTT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_PHU_LUC_HOP_DONG_MTT_SEQ")
    @SequenceGenerator(sequenceName = "HH_PHU_LUC_HOP_DONG_MTT_SEQ", allocationSize = 1, name = "HH_PHU_LUC_HOP_DONG_MTT_SEQ")
    private Long id;
    private Long idHdHdr;
    private String soHdong;
    private String tenHdong;
    private Integer phuLucSo;
    @Temporal(TemporalType.DATE)
    private Date ngayHlucPluc;
    @Temporal(TemporalType.DATE)
    private Date ngayHluc;
    private String veViec;
    @Temporal(TemporalType.DATE)
    private Date ngayHlucGocTu;
    @Temporal(TemporalType.DATE)
    private Date ngayHlucGocDen;
    @Temporal(TemporalType.DATE)
    private Date ngayHlucDcTu;
    @Temporal(TemporalType.DATE)
    private Date ngayHlucDcDen;
    private Integer tgianThucHienGoc;
    private Integer tgianThucHienDc;
    private String noiDungDc;
    private String ghiChu;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private Date ngayTao;
    private String nguoiTao;
    private Date ngayKy;
    private String nguoiKy;

    @Transient
    List<HhDCDiaDiemGiaoNhanHang> dcDiaDiemGiaoNhanHangList=new ArrayList<>();
    @Transient
    private List<FileDinhKem> FileDinhKems =new ArrayList<>();


}
