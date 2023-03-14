package com.tcdt.qlnvhang.table;


import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_QD_PHE_DUYET_KHMTT_HDR")
@Data
public class HhQdPheduyetKhMttHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PHE_DUYET_KHMTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PHE_DUYET_KHMTT_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PHE_DUYET_KHMTT_SEQ", allocationSize = 1, name="HH_QD_PHE_DUYET_KHMTT_SEQ")

    private Long id;

    private Integer namKh;

    private String soQd;

    @Temporal(TemporalType.DATE)
    private Date ngayQd;

    @Temporal(TemporalType.DATE)
    private Date ngayHluc;

    private Long idThHdr;

    private Long idTrHdr;

    private String soTrHdr;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String trichYeu;

    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;

    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private String nguoiSua;

    @Temporal(TemporalType.DATE)
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyet;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String tchuanCluong;

    private Boolean lastest;

    private String phanLoai;

    private String ptMua;

    private String diaDiemCgia;

    private String ghiChu;

    private String soQdPdKqCg;

    private String ptMuaTrucTiep;


    private Long idGoc;

    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();

    @Transient
    private List<HhQdPheduyetKhMttDx> children = new ArrayList<>();

    @Transient
     private  HhQdPheduyetKhMttDx hhQdPheduyetKhMttDxList;

    public String getTenTrangThai() {
        return NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(this.getTrangThai());
    }
}
