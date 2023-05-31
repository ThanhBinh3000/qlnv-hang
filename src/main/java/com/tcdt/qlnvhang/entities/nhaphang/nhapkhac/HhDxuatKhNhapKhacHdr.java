package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = HhDxuatKhNhapKhacHdr.TABLE_NAME)
@Data
public class HhDxuatKhNhapKhacHdr {
    public static final String TABLE_NAME = "HH_DX_KHNK_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHNK_HDR_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHNK_HDR_SEQ", allocationSize = 1, name = "HH_DX_KHNK_HDR_SEQ")
    private Long id;
    private Integer namKhoach;
    private String maDviDxuat;
    @Transient
    private String tenDvi;
    private String loaiHinhNx;
    @Transient
    private String tenLoaiHinhNx;
    private String kieuNx;
    @Transient
    private String tenKieuNx;
    private String soDxuat;
    private String trichYeu;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String dvt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDxuat;
    private String trangThai;
    private Long thopId;
    @Transient
    private String maTh;
    private String trangThaiTh;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTrangThaiTh;
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
    private String lyDoTuChoi;
    private BigDecimal tongSlNhap;
    private BigDecimal tongThanhTien;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();
}
