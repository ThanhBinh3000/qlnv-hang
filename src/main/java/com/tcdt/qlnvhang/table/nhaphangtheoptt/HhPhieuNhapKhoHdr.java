package com.tcdt.qlnvhang.table.nhaphangtheoptt;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "HH_PHIEU_NHAP_KHO_HDR")
@Data
public class HhPhieuNhapKhoHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_PHIEU_NHAP_KHO_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_PHIEU_NHAP_KHO_HDR_SEQ")
//    @SequenceGenerator(sequenceName = "HH_PHIEU_NHAP_KHO_HDR_SEQ", allocationSize = 1, name = "HH_PHIEU_NHAP_KHO_HDR_SEQ")

    private Long id;

    private Long idQdGiaoNvNh;

    private Long idDdiemGiaoNvNh;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;
    @Transient
    private String tenDviCapCha;

    private String maQhns;

    private String soPhieuNhapKho;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNkho;

    private BigDecimal no;

    private BigDecimal co;

    private String soQuyetDinhNhap;

    private String soHdong;

    @Temporal(TemporalType.DATE)
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

    private String soPhieuKtraCluong;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String canBoLapPhieu;

    private String lanhDaoChiCuc;

    private String ktvBaoQuan;

    private String keToanTruong;

    private String hoTenNguoiGiao;

    private String cmt;

    private String donViGiao;

    private String diaChiNguoiGiao;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thoiGianGiaoNhan;

    private String soBangKeCanHang;

    private String ghiChu;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    private String nguoiTao;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private String nguoiPduyet;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngaySua;
    private  String nguoiSua;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private String lyDoTuChoi;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGdinh;


    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<HhPhieuNhapKhoCt> hhPhieuNhapKhoCtList = new ArrayList<>();

    @Transient
    private HhBcanKeHangHdr hhBcanKeHangHdr;


    @Transient
    private BigDecimal tongSl;
    @Transient
    private BigDecimal tongSlct;
    @Transient
    private String tongSlBc;
    @Transient
    private BigDecimal tongSt;
    @Transient
    private String tongStBc;
}
