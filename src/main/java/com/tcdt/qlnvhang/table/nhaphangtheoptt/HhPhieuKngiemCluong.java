package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_PHIEU_KNGHIEM_CLUONG")
@Data
public class HhPhieuKngiemCluong implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_PHIEU_KNGHIEM_CLUONG";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_PHIEU_KN_CLUONG_SEQ")
    @SequenceGenerator(sequenceName = "HH_PHIEU_KN_CLUONG_SEQ", allocationSize = 1, name = "HH_PHIEU_KN_CLUONG_SEQ")

    private Long id;
    private String soPhieu;
    private Integer namKh;
    private Long idBienBan;
    private String soBienBan;
    private Long idQdNh;
    private String soQdNh;
    private String maDvi;
    private String maQhns;
    private String maDiemKho;
    @Transient
    private String tenDiemKho;
    private String maNhaKho;
    @Transient
    private String tenNhaKho;
    private String maNganKho;
    @Transient
    private String tenNganKho;
    private String maLoKho;
    @Transient
    private String tenLoKho;
    private String soLuongBq;
    private String hthucBquan;
    private String thuKho;
    @Temporal(TemporalType.DATE)
    private Date ngayNhapDay;
    @Temporal(TemporalType.DATE)
    private Date ngayLayMau;
    @Temporal(TemporalType.DATE)
    private Date ngayKnMau;
    private String ketQuaDanhGia;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;
    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private  String nguoiSua;
    private String ldoTuChoi;
    @Temporal(TemporalType.DATE)
    private Date ngayGduyet;
    private String nguoiGduyet;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyet;
    @Transient
    List<HhPhieuKnCluongDtl> hhPhieuKnCluongDtlList= new ArrayList<>();

}
