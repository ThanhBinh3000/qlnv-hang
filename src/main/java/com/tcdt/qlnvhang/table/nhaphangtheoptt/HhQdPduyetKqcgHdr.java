package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttSLDDReq;
import com.tcdt.qlnvhang.request.HhQdPheduyetKqMttSLDDReq;
import com.tcdt.qlnvhang.table.FileDinhKem;

import com.tcdt.qlnvhang.table.HhQdPheduyetKqMttSLDD;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_QD_PDUYET_KQCG_HDR")
@Getter
@Setter
public class HhQdPduyetKqcgHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PDUYET_KQCG_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PDUYET_KQCG_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PDUYET_KQCG_SEQ", allocationSize = 1, name = "HH_QD_PDUYET_KQCG_SEQ")
    private Long id;

    private Long idPdKhDtl;

    private Long idPdKhHdr;

    private Integer namKh;

    private String soQdKq;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayKy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayHluc;

    private String soQd;

    private String trichYeu;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String diaDiemChaoGia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayMua;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String ghiChu;

    private String trangThaiHd;
    @Transient
    private String tenTrangThaiHd;

    private String trangThaiNh;
    @Transient
    private String tenTrangThaiNh;

    @Transient
    private Integer slHd;

    @Transient
    private Integer slHdDaKy;

    @Transient
    private BigDecimal tongGtriHd;

//    preview
    @Transient
    private String dvt;
    @Transient
    private BigDecimal soLuong;
    @Transient
    private BigDecimal tongThanhTien;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private FileDinhKem fileDinhKem;

    @Transient
    private List<HopDongMttHdr> hopDongMttHdrList = new ArrayList<>();

    @Transient
    private List<HhQdPheduyetKqMttSLDD> danhSachCtiet = new ArrayList<>();

}
