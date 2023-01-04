package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.FileDinhKem;
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
    @Temporal(TemporalType.DATE)
    private Date ngayNghiemThu;
    private String thuTruong;
    private String keToan;
    private String kyThuatVien;
    private String thuKho;
    private String lhKho;
    private Double slThucNhap;
    private Double tichLuong;
    private String pthucBquan;
    private String hthucBquan;
    private Double dinhMuc;
    private String ketLuan;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String ldoTuchoi;
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
    private Date ngayTao;
    private String nguoiTao;
    private Date ngaySua;
    private String nguoiSua;
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;
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

    @Transient
    private List<FileDinhKem> fileDinhKems =new ArrayList<>();

    @Transient
    private List<HhBbanNghiemThuDtl> dviChuDongThucHien =new ArrayList<>();

    @Transient
    private List<HhBbanNghiemThuDtl> dmTongCucPdTruocThucHien =new ArrayList<>();
}
