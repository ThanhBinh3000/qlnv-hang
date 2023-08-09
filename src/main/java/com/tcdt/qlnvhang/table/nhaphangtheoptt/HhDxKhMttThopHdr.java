package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_DX_KHMTT_THOP_HDR")
@Data
public class HhDxKhMttThopHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_THOP_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_THOP_HDR_SEQ")
//    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_THOP_HDR_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_THOP_HDR_SEQ")
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayThop;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String noiDungThop;

    private Integer namKh;

    private String maDvi;

    private String soQdPduyet;

    private String soQdCc;
    private Long idSoQdCc;

    private String tchuanCluong;
    private Long idQdPduyet;

    @Transient
    List<HhDxKhMttThopDtl> children =new ArrayList<>();
    @Transient
    Long qdPdMttId;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

}
