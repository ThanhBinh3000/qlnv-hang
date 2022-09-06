package com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhPhieuXuatKho.TABLE_NAME)
@EqualsAndHashCode(callSuper = true)
@Data
public class XhPhieuXuatKho extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1036129798681529685L;
    public static final String TABLE_NAME = "XH_PHIEU_XUAT_KHO";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_XUAT_KHO_SEQ")
    @SequenceGenerator(sequenceName = "XH_PHIEU_XUAT_KHO_SEQ", allocationSize = 1, name = "XH_PHIEU_XUAT_KHO_SEQ")
    @Column(name = "id", nullable = false)
    private Long id;
    // Số quyết định xuất id
    @Column(name = "SO_QD_XUAT_ID")
    private Long sqdxId;
    @Column(name = "PHIEU_KN_CL_ID")
    // phiếu kiểm nghiệm chất lượng id
    private Long pknclId;
    @Column(name = "MA_DVI")
    private String maDvi;
    @Column(name = "MA_QHNS")
    private String maQHNS;
    @Column(name = "SO_HOP_DONG")
    private String soHd;

    @Column(name = "SO_PHIEU_XUAT_KHO")
    private String spXuatKho;
    @Column(name = "NGUOI_NHAN_HANG")
    private String nguoiNhanHang;
    @Column(name = "BO_PHAN")
    private String boPhan;


    @Column(name = "MA_DIEMKHO")
    private String maDiemkho;
    @Column(name = "MA_NHAKHO")
    private String maNhakho;
    @Column(name = "MA_NGANKHO")
    private String maNgankho;
    @Column(name = "MA_LOKHO")
    private String maNganlo;
    @Column(name = "MA_LOAI_HANG_HOA")
    private String maLoaiHangHoa;
    @Column(name = "MA_CHUNG_LOAI_HANG_HOA")
    private String maChungLoaiHangHoa;


    @Column(name = "XUAT_KHO")
    private LocalDate xuatKho;
    @Column(name = "LY_DO_XUAT_KHO")
    private String lyDoXuatKho;
    @Column(name = "TRANG_THAI")
    private String trangThai;
    @Column(name = "SO")
    private Integer so;
    @Column(name = "NAM")
    private Integer nam;


//    @Column(name = "NGAY_TAO")
//    private LocalDate ngayTao;
//    @Column(name = "NGUOI_TAO_ID")
//    private Long nguoiTaoId;
//    @Column(name = "NGAY_SUA")
//    private LocalDate ngaySua;
//    @Column(name = "NGUOI_SUA_ID")
//    private Long nguoiSuaId;
//    @Column(name = "NGUOI_GUI_DUYET_ID")
//    private Long nguoiGuiDuyetId;
//    @Column(name = "NGAY_GUI_DUYET")
//    private LocalDate ngayGuiDuyet;
//    @Column(name = "NGUOI_PDUYET_ID")
//    private Long nguoiPduyetId;
//    @Column(name = "NGAY_PDUYET")
//    private LocalDate ngayPduyet;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

}
