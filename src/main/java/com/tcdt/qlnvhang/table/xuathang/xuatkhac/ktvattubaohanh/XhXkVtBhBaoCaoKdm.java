package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhXkVtBhBaoCaoKdm.TABLE_NAME)
@Data
public class XhXkVtBhBaoCaoKdm extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_XK_VT_BH_BCKQ_KDM";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBhBaoCaoKdm.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhXkVtBhBaoCaoKdm.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhXkVtBhBaoCaoKdm.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maDviNhan;
    private String soBaoCao;
    private String tenBaoCao;
    private String idCanCu;
    private String soCanCu;
    private String loaiCanCu;
    private LocalDate ngayBaoCao;
    private String trangThai;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private Long idQdXuatGiamVt;
    private String soQdXuatGiamVt;
    
    @Transient
    private String tenDvi;
    @Transient
    private String tenDviNhan;
    @Transient
    private String tenTrangThai;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private List<XhXkVtBhQdGiaonvXnHdr> qdGiaonvXn = new ArrayList<>();
    @Transient
    private List<XhXkVtBhPhieuKtclHdr> phieuKtcl = new ArrayList<>();

}
