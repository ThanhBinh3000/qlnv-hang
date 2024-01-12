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
@Table(name = HhThopKhNhapKhac.TABLE_NAME)
@Data
public class HhThopKhNhapKhac {
    public static final String TABLE_NAME = "HH_THOP_KHNK";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_THOP_KHNK_SEQ")
    @SequenceGenerator(sequenceName = "HH_THOP_KHNK_SEQ", allocationSize = 1, name = "HH_THOP_KHNK_SEQ")
    private Long id;
    private Integer namKhoach;
    private String loaiHinhNx;
    @Transient
    private String tenLoaiHinhNx;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String maTh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTh;
    private String noiDungTh;
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
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String dvt;
    private BigDecimal tongSlNhap;
    private Long idQd;
    private String soQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private List<HhDxuatKhNhapKhacHdr> dxHdr;
}
