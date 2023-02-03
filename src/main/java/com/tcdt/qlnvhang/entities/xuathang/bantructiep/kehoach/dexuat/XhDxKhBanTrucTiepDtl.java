package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_DX_KH_BAN_TRUC_TIEP_DTL")
@Data
public class XhDxKhBanTrucTiepDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_TRUC_TIEP_DTL ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_TRUC_TIEP_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_TRUC_TIEP_DTL_SEQ", allocationSize = 1, name = "XH_DX_KH_BAN_TRUC_TIEP_DTL_SEQ")
    private Long id;

    private Long idHdr;

    private BigDecimal soLuong;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String diaChi;

    @Transient
    private List<XhDxKhBanTrucTiepDdiem> children = new ArrayList<>();

}
