package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "DCNB_TH_KE_HOACH_DCC_NBC_DTL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class THKeHoachDieuChuyenNoiBoCucDtl implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_TH_KH_DCC_NBC_DTL_SEQ")
    @SequenceGenerator(sequenceName = "DCNB_TH_KH_DCC_NBC_DTL_SEQ", allocationSize = 1, name = "DCNB_TH_KH_DCC_NBC_DTL_SEQ")
    private Long id;

    @Column(name = "DCNB_TH_KE_HOACH_DCC_HDR_ID")
    private Long hdrId;

    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID")
    private Long dcKeHoachDcHdrId;

    @Column(name = "DCNB_KE_HOACH_DC_DTL_ID")
    private Long dcKeHoachDcDtlId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DCNB_TH_KE_HOACH_DCC_HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private THKeHoachDieuChuyenCucHdr tHKeHoachDieuChuyenCucHdr;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DCNB_KE_HOACH_DC_HDR_ID", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler","ngayTao","nguoiTaoId","ngaySua","nguoiSuaId","danhSachHangHoa","phuongAnDieuChuyen","canCu"})
    @NotFound(action = NotFoundAction.IGNORE)
    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DCNB_KE_HOACH_DC_DTL_ID", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler","ngayTao","nguoiTaoId","ngaySua","nguoiSuaId"})
    @NotFound(action = NotFoundAction.IGNORE)
    private DcnbKeHoachDcDtl dcnbKeHoachDcDtl;
}
