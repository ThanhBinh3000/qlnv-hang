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
@Table(name = "HH_BIEN_BAN_NGHIEM_THU")
@Data
public class HhBienBanNghiemThu implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_BIEN_BAN_NGHIEM_THU";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BIEN_BAN_NGHIEM_THU_SEQ")
    @SequenceGenerator(sequenceName = "HH_BIEN_BAN_NGHIEM_THU_SEQ", allocationSize = 1, name = "HH_BIEN_BAN_NGHIEM_THU_SEQ")
    private Long id;
    private String soBb;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNghiemThu;
    private String thuTruong;
    private String keToan;
    private String kyThuatVien;
    private String thuKho;
    private String lhKho;
    private Double slCanNhap;
    private Long idPhieuNhapKho;
    private String soPhieuNhapKho;
    private Double slThucNhap;
    private Double tichLuong;
    private String pthucBquan;
    private String hthucBquan;
    private Double dinhMuc;
    private String ketLuan;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String ldoTuChoi;
    private String capDvi;
    private String maDvi;
    private Integer namKh;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayTao;
    private String nguoiTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngaySua;
    private String nguoiSua;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayPduyet;
    private String nguoiPduyet;

    private Long hopDongId;

    @Transient
    private String soHopDong;

    @Transient
    private String tenDvi;

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

    private String maQhns;

    private Long idQdGiaoNvNh;

    private String soQdGiaoNvNh;

    private Long idDdiemGiaoNvNh;

    @Transient
    private FileDinhKem fileDinhKem;

    private BigDecimal kinhPhiThucTe;

    private BigDecimal kinhPhiTcPd;
    private String soBangKe;

    @Transient
    private List<FileDinhKem> fileDinhKems =new ArrayList<>();

    @Transient
    private List<HhBbanNghiemThuDtl> dviChuDongThucHien =new ArrayList<>();

    @Transient
    private List<HhBbanNghiemThuDtl> dmTongCucPdTruocThucHien =new ArrayList<>();

    @Transient
    private Integer ngay;
    @Transient
    private Integer thang;
    @Transient
    private Integer nam;
    @Transient
    private BigDecimal tongSoLuong;
    @Transient
    private String tongThanhTienStr;
    @Transient
    private BigDecimal tongSlNamTruoc;
    @Transient
    private String tongThanhTienNamTruocStr;
    @Transient
    private String tongGiaTriStr;
    @Transient
    private String kinhPhiThucTeStr;
    @Transient
    private String kinhPhiTtStr;
}
