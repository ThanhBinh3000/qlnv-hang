package com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "QD_PHE_DUYET_KQLCNT_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QdPheDuyetKqlcntVt extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8891387383793081263L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_PHE_DUYET_KQLCNT_VT_SEQ")
    @SequenceGenerator(sequenceName = "QD_PHE_DUYET_KQLCNT_VT_SEQ", allocationSize = 1, name = "QD_PHE_DUYET_KQLCNT_VT_SEQ")
    private Long id;
    private String soQuyetDinh;
    private LocalDate ngayQuyetDinh;
    private Integer namKeHoach;
    private String veViec;
    private String trangThai;
    private Long vatTuId; // QLNV_DM_DONVI
    private String maVatTu;
    private Long khLcntVtId; // KH_LCNT_VT
    private Long thongTinDauThauId; //THONG_TIN_DAU_THAU_VT
    private LocalDate ngayGuiDuyet;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayPheDuyet;
    private Long nguoiPheDuyetId;
    private String lyDoTuChoi;
    private String maDonVi;
    private String capDonVi;
    @Transient
    private List<QdKqlcntGoiThauVt> goiThauList = new ArrayList<>();
}
