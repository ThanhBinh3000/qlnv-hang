package com.tcdt.qlnvhang.table;


import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
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
    private static final long serialVersionUID = 1;
    public static final String TABLE_NAME = "HH_QD_PHE_DUYET_KHMTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PHE_DUYET_KHMTT_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PHE_DUYET_KHMTT_SEQ", allocationSize = 1, name="HH_QD_PHE_DUYET_KHMTT_SEQ")

    private Long id;
    private Integer namKh;
    private String soQd;
    private String soDxuat;
    @Temporal(TemporalType.DATE)
    private Date ngayKyQd;
    @Temporal(TemporalType.DATE)
    private Date ngayHluc;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;


    private String nguoiTao;


    @Temporal(TemporalType.DATE)
    private Date ngaySua;


    private String nguoiSua;


    private String lyDoTuChoi;


    @Temporal(TemporalType.DATE)
    private Date ngayGuiDuyet;



    private  String nguoiGuiDuyet;


    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;


    private String nguoiPduyet;


    private String maTongHop;


    private String trichYeu;


    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;


    private String cloaiVthh;
    @Transient
    private  String tenCloaiVthh;


    private String moTaHhoa;


    private String PhuongThucMua;


    private String tieuChuanCl;


    private Long giaMua;


    private Long giaChuaVat;


    private Long thueGtgt;


    private  Long giaDaVat;


    @Temporal(TemporalType.DATE)
    private Date thoiDiemMoCuaKho;


    @Temporal(TemporalType.DATE)
    private  Date thoiDiemMuaThoc;


    private String ghiChu;


    private  String maDvi;

    private String tenDvi;


    private  Long tongMucDt;


    private  Long tongSlMuaTt;


    private String nguonVon;


    private String trangThai;
    @Transient
    private String tenTrangThai;


    private String trangThaiTh;
    @Transient
    private String tenTrangThaiTh;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<HhQdPheduyetKhMttDx> hhQdPheduyetKhMttDxList = new ArrayList<>();

    @Transient
    private List<HhQdPheduyetKhMttSLDD> hhQdPheduyetKhMttSLDDList = new ArrayList<>();
}
