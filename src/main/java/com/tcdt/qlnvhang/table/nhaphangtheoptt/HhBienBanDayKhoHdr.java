package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
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
@Table(name = "HH_BIEN_BAN_DAY_KHO_HDR")
@Data
public class HhBienBanDayKhoHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_BIEN_BAN_DAY_KHO_HDR";
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BIEN_BAN_DAY_KHO_HDR_SEQ")
//    @SequenceGenerator(sequenceName = "HH_BIEN_BAN_DAY_KHO_HDR_SEQ", allocationSize = 1, name = "HH_BIEN_BAN_DAY_KHO_HDR_SEQ")


    private Long id;

    private Long idQdGiaoNvNh;

    private Long idPhieuNhapKho;

    private Long idBangCanKeHang;

    private Long idDdiemGiaoNvNh;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String maQhns;

    private String soBbNhapDayKho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLapBban;

    private String soQuyetDinhNhap;

    private String soHdong;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKiHdong;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayBdauNhap;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKthucNhap;

    private BigDecimal tongSoLuongNhap;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String ghiChu;

    private String ktvBanQuan;

    private String keToanTruong;
    private String soBangKe;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    private String nguoiTao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngaySua;
    private String nguoiSua;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private String nguoiPduyet;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private String lyDoTuChoi;

    private String soPhieuNhapKho;

    private String soBangKeCanHang;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNkho;




    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<HhBienBanDayKhoDtl> hhBienBanDayKhoDtlList = new ArrayList<>();





}
