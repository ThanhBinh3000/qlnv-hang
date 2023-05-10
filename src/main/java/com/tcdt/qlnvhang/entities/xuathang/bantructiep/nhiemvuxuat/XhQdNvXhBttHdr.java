package com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "XH_QD_NV_XH_BTT_HDR")
@Data
public class XhQdNvXhBttHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_NV_XH_BTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_NV_XH_BTT_HDR_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_NV_XH_BTT_HDR_SEQ", allocationSize = 1, name = "XH_QD_NV_XH_BTT_HDR_SEQ")

    private Long id;

    private String maDvi;
    @Transient
    private String tenDvi;

    private Integer namKh;

    private String soQdNv;

    private Long idHd;

    private String soHd;

    private Long idQdPd;

    private Long idQdPdDtl;

    private String soQdPd;

    private String maDviTsan;

    private String tenTccn;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String loaiHinhNx;

    private String kieuNx;

    private BigDecimal soLuongBanTrucTiep;

    private String donViTinh;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGnhan;

    private String trichYeu;

    private String trangThaiXh;
    @Transient
    private String tenTrangThaiXh;

    private String phanLoai;

    private String trangThaiHd;
    @Transient
    private String tenTrangThaiHd;

    @Transient
    private List<XhQdNvXhBttDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    @Transient
    private List<XhHopDongBttHdr> listHopDongBtt;
}
