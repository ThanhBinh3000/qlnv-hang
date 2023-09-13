package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDcQdPduyetKhmttSldd;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_QD_PHE_DUYET_KHMTT_DX")
@Data

public class HhQdPheduyetKhMttDx implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PHE_DUYET_KHMTT_DX";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PHE_DUYET_KHMTT_DX_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PHE_DUYET_KHMTT_DX_SEQ", allocationSize = 1, name = "HH_QD_PHE_DUYET_KHMTT_DX_SEQ")

    private Long id;

    private Long idQdHdr;

    private Long idDxHdr;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String diaChi;

    private String soDxuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;

    private String trichYeu;

    private String tenDuAn;

    private BigDecimal tongSoLuong;

    private BigDecimal tongTienGomThue;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenNguonVon;

    private String moTaHangHoa;

    private String ptMua;

    private String tchuanCluong;

    private String giaMua;

    private BigDecimal donGia;

    private BigDecimal thueGtgt;

    private BigDecimal donGiaVat;

    private String ghiChu;

    private String nguonVon;

    private BigDecimal tongMucDt;

    private Integer namKh;

    @Transient
    private List<HhQdPheduyetKhMttSLDD> children = new ArrayList<>();

    @Transient
    private List<HhDcQdPduyetKhmttSldd> children2 = new ArrayList<>();


    // thông tin chào giá
    private String pthucMuaTrucTiep;
    @Transient
    private String tenPthucMuaTrucTiep;

    private String soQd;

    private String diaDiemChaoGia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhanCgia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayMua;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKthuc;

    private String ghiChuChaoGia;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private String soQdKq;

    @Transient
    private List<FileDinhKem> fileDinhKemUyQuyen = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKemMuaLe = new ArrayList<>();

    @Transient
    private List<HhChiTietTTinChaoGia> listChaoGia = new ArrayList<>();

    @Transient
    private HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr;

    @Transient
    private String tongThanhTien;
}
