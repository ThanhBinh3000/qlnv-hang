package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.math.BigDecimal;
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

    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;

    @Transient
    private  String tenCloaiVthh;

    private String  moTaHangHoa;

    private String tchuanCluong;

    private Boolean lastest;

    private String phanLoai;

    private Long idGoc;

    @Transient
    List<XhQdPdKhBdgDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();

}
