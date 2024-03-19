package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_BIEN_BAN_LAY_MAU")
@Data
public class HhBienBanLayMau implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_BIEN_BAN_LAY_MAU";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BIEN_BAN_LAY_MAU_SEQ")
    @SequenceGenerator(sequenceName = "HH_BIEN_BAN_LAY_MAU_SEQ", allocationSize = 1, name = "HH_BIEN_BAN_LAY_MAU_SEQ")

    private Long id;
    private Integer namKh;
    private String loaiBienBan;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String soBienBan;
    private String maQhns;
    private String soBangKe;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    @Temporal(TemporalType.DATE)
    private Date ngayQdNh;
    private String soHd;
    private String soBbNhapDayKho;
    private Long idBbNhapDayKho;
    @Temporal(TemporalType.DATE)
    private Date ngayKetThucNhap;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    @Temporal(TemporalType.DATE)
    private Date ngayLayMau;
    private String dviKiemNghiem;
    private String diaDiemLayMau;
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
    private Integer soLuongMau;
    private String ppLayMau;
    private String chiTieuKiemTra;
    private Boolean ketQuaNiemPhong;
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
    private String truongBpKtbq;
    private String tenDviCcHang;

    @Transient
    private FileDinhKem fileDinhKem;

    @Transient
    private List<FileDinhKem> fileDinhKems =new ArrayList<>();

    @Transient
    private List<HhBbanLayMauDtl> bbanLayMauDtlList = new ArrayList<>();

    @Transient
    private HhBienBanDayKhoHdr bbNhapDayKho;

    @Transient
    private String tenCloaiVthhUp;
    @Transient
    private String tenDviUp;
    @Transient
    private String tenDviCha;
    @Transient
    private String ngayLayMauStr;
    @Transient
    private List<String> chiTieuKiemTraList;
}
