package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdPdNkHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhThopKhNhapKhac;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = HhQdPdNhapKhacHdr.TABLE_NAME)
@Data
public class HhQdPdNhapKhacHdr {
    public static final String TABLE_NAME = "HH_QD_PDNK_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PDNK_HDR_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PDNK_HDR_SEQ", allocationSize = 1, name = "HH_QD_PDNK_HDR_SEQ")
    private Long id;
    private Integer namKhoach;
    private String soQd;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private Long idDx;
    private String soDxuat;
    private Long idTh;
    private String maTh;
    private Integer tongSlNhap;
    private Integer tongThanhTien;
    private String dvt;
    private String noiDung;
    private String trichYeu;
    private String loaiHinhNx;
    @Transient
    private String tenLoaiHinhNx;
    private String kieuNx;
    @Transient
    private String tenKieuNx;
    private String trangThai;
    private String maDvi;
    private String lyDoTuChoi;
    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;
    @Transient
    private String phanLoai;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHieuLuc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    private String nguoiTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngaySua;
    private String nguoiSua;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private String nguoiPduyet;
    private Long idGoc;
    private Boolean lastest;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @JsonManagedReference
    @Where(clause = "data_type='" + HhQdPdNhapKhacHdr.TABLE_NAME + "'")
    private List<FileDKemJoinQdPdNkHdr> fileDinhKems = new ArrayList<>();

    public void setFileDinhKems(List<FileDKemJoinQdPdNkHdr> children) {
        this.fileDinhKems.clear();
        for (FileDKemJoinQdPdNkHdr child : children) {
            child.setParent(this);
        }
        this.fileDinhKems.addAll(children);
    }

    public void addFileDinhKems(FileDKemJoinQdPdNkHdr child) {
        child.setParent(this);
        this.fileDinhKems.add(child);
    }
    @Transient
    private List<HhQdPdNhapKhacDtl> details = new ArrayList<>();
    @Transient
    private List<HhDxuatKhNhapKhacHdr> children;

}
