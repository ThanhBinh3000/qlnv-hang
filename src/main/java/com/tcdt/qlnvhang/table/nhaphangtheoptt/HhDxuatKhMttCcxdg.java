package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HH_DX_KHMTT_CCXDG")
@Data
public class HhDxuatKhMttCcxdg implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_CCXDG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_CCXDG_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_CCXDG_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_CCXDG_SEQ")
    private Long id;
    private Long  idDxKhmtt;
    private String moTa;
    @Transient
    private List<FileDinhKem> ccFileDinhKems =new ArrayList<>();
}
