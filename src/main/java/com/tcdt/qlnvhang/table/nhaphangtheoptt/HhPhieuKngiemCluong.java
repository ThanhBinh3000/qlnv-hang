package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
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
@Table(name = "HH_PHIEU_KNGHIEM_CLUONG")
@Data
public class HhPhieuKngiemCluong  extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_PHIEU_KNGHIEM_CLUONG";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_PHIEU_KN_CLUONG_SEQ")
    @SequenceGenerator(sequenceName = "HH_PHIEU_KN_CLUONG_SEQ", allocationSize = 1, name = "HH_PHIEU_KN_CLUONG_SEQ")

    private Long id;
    private Integer namKh;
    private String maDvi;
    private String maQhns;
    @Transient
    private String tenDvi;
    private String soBbLayMau;
    private String soQdGiaoNvNh;
    private String soBbNhapDayKho;
    private String soPhieuKiemNghiemCl;
    private Long idKyThuatVien;
    @Transient
    private String tenKyThuatVien;
    private Long idTruongPhong;
    @Transient
    private String tenTruongPhong;
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
    private Long idDdiemGiaoNvNh;
    private Long idQdGiaoNvNh;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String hthucBquan;
    private BigDecimal soLuongNhapDayKho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhapDayKho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLayMau;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKnghiem;
    private String ketLuan;
    private String ketQuaDanhGia;
    @Transient
    List<HhPhieuKnCluongDtl> hhPhieuKnCluongDtlList= new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems =new ArrayList<>();

    @Transient
    private Integer ngay;

    @Transient
    private Integer thang;

    @Transient
    private Integer nam;

    @Transient
    private String tenNganLo;
    @Transient
    private String ngayNhapDayKhoStr;
    @Transient
    private String ngayLayMauStr;
    @Transient
    private String ngayKiemNghiemMauStr;
}
