package com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang;

import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhBkeCanHangBttHdr.TABLE_NAME)
@Data
public class XhBkeCanHangBttHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BKE_CAN_HANG_BTT_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =TABLE_NAME +"_SEQ")
//    @SequenceGenerator(sequenceName = TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")

    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String maQhns;

    private String soBangKe;

    private Long idQdNv;

    private String soQdNv;

    private LocalDate ngayQdNv;

    private Long idHd;

    private String soHd;

    private LocalDate ngayKyHd;

    private Long idDdiemXh;

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

    private Long idPhieuXuat;

    private String soPhieuXuat;

    private LocalDate ngayXuatKho;

    private String diaDiemKho;

    private Long idThuKho;
    @Transient
    private String tenThuKho;

    private String nguoiGiao;

    private String cmtNguoiGiao;

    private String ctyNguoiGiao;

    private String diaChiNguoiGiao;

    private LocalDate tgianGiaoNhan;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String donViTinh;

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
    @Transient
    private String tenNguoiPduyet;

    private String lyDoTuChoi;

    private String phanLoai;

    private String pthucBanTrucTiep;

    private Long idBangKeBanLe;

    private String soBangKeBanLe;

    private LocalDate ngayTaoBangKe;

    @Transient
    private BigDecimal trongLuongCaBaoBi;
    @Transient
    private BigDecimal trongLuongBaoBi;

    @Transient
    private List<XhBkeCanHangBttDtl> children = new ArrayList<>();

    public void setChildren(List<XhBkeCanHangBttDtl> children) {
        this.children = children;
        if (!DataUtils.isNullOrEmpty(children)) {
            this.trongLuongCaBaoBi = children.stream().map(XhBkeCanHangBttDtl::getTrongLuongCaBaoBi).reduce(BigDecimal.ZERO, BigDecimal::add);
            this.trongLuongBaoBi = children.stream().map(XhBkeCanHangBttDtl::getTrongLuongBaoBi).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
