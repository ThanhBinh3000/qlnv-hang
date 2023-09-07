package com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKho;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhXkVtBckqXuatHangKdm.TABLE_NAME)
@Data
public class XhXkVtBckqXuatHangKdm extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_XK_BCKQ_XH_KDM_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBckqXuatHangKdm.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhXkVtBckqXuatHangKdm.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhXkVtBckqXuatHangKdm.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maDviNhan;
    private String soBaoCao;
    private String tenBaoCao;
    private Long idDsTh;
    private String maDsTh;
    private LocalDate ngayBaoCao;
    private String trangThai;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;

    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private List<XhXkThXuatHangKdmDtl> xhXkThXuatHangKdmDtl = new ArrayList<>();

}
