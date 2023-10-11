package com.tcdt.qlnvhang.table;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.util.Contains;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_QD_PHE_DUYET_KHMTT_HDR")
@Data
public class HhQdPheduyetKhMttHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PHE_DUYET_KHMTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PHE_DUYET_KHMTT_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PHE_DUYET_KHMTT_SEQ", allocationSize = 1, name="HH_QD_PHE_DUYET_KHMTT_SEQ")

    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String soQd;
    @Transient
    private String soQdDc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayQd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private Long idThHdr;

    private String soTrHdr;

    private Long idTrHdr;

    private String trichYeu;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String  moTaHangHoa;

    private String tchuanCluong;

    private Boolean lastest;
    private Boolean isChange;

    private String phanLoai;

    private Long idGoc;
    private Long idSoQdCc;
    private String soQdCc;

    private Long idQdGnvu;
    private String soQdGnvu;

    private String ptMuaTrucTiep;
    @Transient
    private String tenPtMuaTrucTiep;

    private String trangThaiHd;

    @Transient
    private String tenTrangThaiHd;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<HhQdPheduyetKhMttDx> children = new ArrayList<>();

}
