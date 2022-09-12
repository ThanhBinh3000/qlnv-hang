package com.tcdt.qlnvhang.entities.xuathang.bangkecanhang;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhBangKeCanHang.TABLE_NAME)
@EqualsAndHashCode(callSuper = true)
@Data
public class XhBangKeCanHang extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1036129798681529685L;
    public static final String TABLE_NAME = "XH_BANG_KE_CAN_HANG";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BANG_KE_CAN_HANG_SEQ")
    @SequenceGenerator(sequenceName = "XH_BANG_KE_CAN_HANG_SEQ", allocationSize = 1, name = "XH_BANG_KE_CAN_HANG_SEQ")
    @Column(name = "id", nullable = false)
    private Long id;
    // Số quyết định xuất id
    @Column(name = "SO_QD_XUAT_ID")
    private Long sqdxId;
    @Column(name = "PHIEU_XUAT_KHO_ID")
    private Long phieuXuatKhoId;

    @Column(name = "MA_DVI")
    private String maDvi;
    @Column(name = "MA_QHNS")
    private String maQHNS;
    @Column(name = "SO_BANG_KE")
    private String soBangKe;

    @Column(name = "THU_KHO_ID")
    private String thuKhoId;
    @Column(name = "MA_LOKHO")
    private String maLokho;
    @Column(name = "MA_NGANKHO")
    private String maNgankho;
    @Column(name = "MA_NHAKHO")
    private String maNhakho;
    @Column(name = "MA_DIEMKHO")
    private String maDiemkho;
    @Column(name = "MA_CHUNG_LOAI_HANG_HOA")
    private String maChungLoaiHangHoa;
    @Column(name = "MA_LOAI_HANG_HOA")
    private String maLoaiHangHoa;
    @Column(name = "DIA_DIEM")
    private String diaDiem;
    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "NAM")
    private Integer nam;
    @Column(name = "SO_HOP_DONG")
    private String soHopDong;
    @Column(name = "NGAY_NHAP")
    private LocalDate ngayNhap;
    @Column(name = "DVI_TINH_ID")
    private String dviTinhId;
    // trọng lượng bao bì
    @Column(name = "TL_BB")
    private Integer tlBb;
    // trọng lượng trừ bao bì
    @Column(name = "TL_TRU_BB")
    private Integer tlTruBb;
    // trọng lượng kể cả bao bì
    @Column(name = "TL_KE_CA_BB")
    private Integer tlKeCaBb;

    @Column(name = "TEN_NGUOI_NHAN")
    private String tenNguoiNhan;
    @Column(name = "DIA_CHI_NGUOI_NHAN")
    private String diaChiNguoiNhan;

    @Transient
    private List<XhBangKeCanHangCt> ds = new ArrayList<>();
}
