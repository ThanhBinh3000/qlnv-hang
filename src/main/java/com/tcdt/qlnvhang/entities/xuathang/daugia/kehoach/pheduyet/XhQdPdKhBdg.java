package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet;

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
@Table(name = "XH_QD_PD_KH_BDG")
@Data
public class XhQdPdKhBdg extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BDG_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BDG_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BDG_SEQ")
    private Long id;

    private  Integer nam;

    private String maDvi;

    @Transient
    private String tenDvi;

    private String soQdPd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private Long idThHdr;

    private String soTrHdr;

    private Long idTrHdr;

    private String trichYeu;

    private String loaiVthh;

    private String cloaiVthh;

    private String  moTaHangHoa;

    private String tchuanCluong;

    private Boolean lastest;

    private String phanLoai;

    private Long idGoc;

    private String soQdCc;

    // Transient

    @Transient
    private String tenLoaiVthh;

    @Transient
    private  String tenCloaiVthh;

    @Transient
    List<XhQdPdKhBdgDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private FileDinhKem fileDinhKem;

}
