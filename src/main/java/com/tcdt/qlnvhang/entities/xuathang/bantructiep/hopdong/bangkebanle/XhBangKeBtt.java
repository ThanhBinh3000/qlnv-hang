package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.bangkebanle;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "XH_BANG_KE_BTT")
@Data
public class XhBangKeBtt  implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BANG_KE_BTT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BANG_KE_BTT_SEQ")
    @SequenceGenerator(sequenceName = "XH_BANG_KE_BTT_SEQ", allocationSize = 1, name = "XH_BANG_KE_BTT_SEQ")

    private Long id;

    private Integer namKh;

    private String soBangKe;

    private String soQd;

    private String maDvi;
    @Transient
    private String tenDvi;

    private BigDecimal soLuong;

    private BigDecimal soLuongConLai;

    private String nguoiPhuTrach;

    private String diaChi;

    private LocalDate ngayBanHang;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private BigDecimal soLuongBtt;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String tenNguoiMua;

    private String diaChiNguoiMua;

    private String cmt;

    private String ghiChu;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private LocalDate ngayTao;

    private Long nguoiTaoId;

    private LocalDate ngaySua;

    private Long nguoiSuaId;

    private LocalDate ngayGuiDuyet;

    private Long nguoiGuiDuyetId;

    private LocalDate ngayPduyet;

    private Long nguoiPduyetId;

    private String lyDoTuChoi;
}
