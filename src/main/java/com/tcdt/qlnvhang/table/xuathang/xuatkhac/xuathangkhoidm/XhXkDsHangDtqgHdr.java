package com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangDtl;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = XhXkDsHangDtqgHdr.TABLE_NAME)
public class XhXkDsHangDtqgHdr extends BaseEntity implements Serializable {

    public static final String TABLE_NAME = "XH_XK_DS_HANG_DTQG_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_XK_DS_HANG_DTQG_HDR_SEQ")
    @SequenceGenerator(sequenceName = "XH_XK_DS_HANG_DTQG_HDR_SEQ", allocationSize = 1, name = "XH_XK_DS_HANG_DTQG_HDR_SEQ")
    private Long id;
    private String maDs;
    private String tenDs;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;
    @OneToMany(mappedBy = "xhXkDsHangDtqgHdr", cascade = CascadeType.ALL)
    private List<XhXkDsHangDtqgDtl> xhXkDsHangDtqgDtl = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems;
    @Transient
    private String tenTrangThai;
}
