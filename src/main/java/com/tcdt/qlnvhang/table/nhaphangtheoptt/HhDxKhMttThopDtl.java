package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HH_DX_KHMTT_THOP_DTL")
@Data
public class HhDxKhMttThopDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_THOP_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_THOP_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_THOP_DTL_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_THOP_DTL_SEQ")
    private Long id;
    private Long idThopHdr;
    private Long idDxHdr;
    private String soDxuat;
    private String maDvi;
    @Transient
    String tenDvi;

    @Transient
    private List<HhDxuatKhMttHdr> listDxuatHdr= new ArrayList<>();


}
