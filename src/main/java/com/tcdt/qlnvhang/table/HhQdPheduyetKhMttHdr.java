package com.tcdt.qlnvhang.table;


import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
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
    private String soQdPduyet;
    private Long idDxuat;
    private String soDxuat;
    private Long idThop;
    private String maThop;
    private String maDvi;
    @Transient
    private String tenDvi;
    @Temporal(TemporalType.DATE)
    private Date ngayKy;
    @Temporal(TemporalType.DATE)
    private Date ngayHluc;
    private String trichYeu;

    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;

    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private String nguoiSua;

    @Temporal(TemporalType.DATE)
    private Date ngayDuyet;
    private String nguoiDuyet;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private String trangThaiTkhai;
    @Transient
    private String tenTrangThaiTkhai;

    private String pthucMuatt;

    private String diaDiemCgia;

    private String ghiChu;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String soQdPdCg;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<HhQdPheduyetKhMttDx> hhQdPheduyetKhMttDxList = new ArrayList<>();


    @Transient
    private List<HhChiTietTTinChaoGia> hhChiTietTTinChaoGiaList = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKemUyQuyen = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKemMuaLe = new ArrayList<>();
}
