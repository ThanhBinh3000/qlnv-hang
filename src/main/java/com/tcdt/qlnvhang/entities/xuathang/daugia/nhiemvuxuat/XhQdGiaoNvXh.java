package com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = XhQdGiaoNvXh.TABLE_NAME)
@Data
public class XhQdGiaoNvXh extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_GIAO_NV_XH";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_GIAO_NV_XH_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_GIAO_NV_XH_SEQ", allocationSize = 1, name = "XH_QD_GIAO_NV_XH_SEQ")
    private Long id;
    private Integer nam;
    private String soQd;
    private String maDvi;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;
    private Long idHd;
    private String soHd;
    private LocalDate ngayKyHd;
    private String maDviTsan;
    private String tenTtcn;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String donViTinh;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGnhan;
    private String trichYeu;
    private BigDecimal soLuong;

    private String trangThaiXh;

    private String bbTinhKho;

    private String bbHaoDoi;

    private String loaiHinhNx;

    private String kieuNx;

    @Transient
    private String tenTrangThaiXh;

    // Transient
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiVthh;

    @Transient
    private String tenCloaiVthh;

    @Transient
    private List<XhQdGiaoNvXhDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();

}
