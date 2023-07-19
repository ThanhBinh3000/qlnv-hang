package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhXkVtBckqKiemDinhMau.TABLE_NAME)
@Data
public class XhXkVtBckqKiemDinhMau extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_XK_VT_BCKQ_KDM_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBckqKiemDinhMau.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhXkVtBckqKiemDinhMau.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhXkVtBckqKiemDinhMau.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maDviNhan;
    private String soBaoCao;
    private String tenBaoCao;
    private String idQdGiaoNvXh;
    private String soQdGiaoNvXh;
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


}
