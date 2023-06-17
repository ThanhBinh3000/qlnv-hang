package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbPhieuXuatKhoHdr.TABLE_NAME)
@Getter
@Setter
public class DcnbPhieuXuatKhoHdr extends BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_PHIEU_XUAT_KHO_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanLayMauHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanLayMauHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBienBanLayMauHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenMaDvi;
    private String maQhns;
    private String soPhieuXuatKho;
    private LocalDate ngayTaoPhieu;
    private LocalDate ngayXuatKho;
    private Integer taiKhoanNo;
    private Integer taiKhoanCo;
    private Long qddcId;
    private String soQddc;
    private LocalDate ngayKyQddc;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private Long phieuKnChatLuongHdrId;
    private String soPhieuKnChatLuong;
    private LocalDate ngayKyPhieuKnChatLuong;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private String canBoLapPhieu;
    private Long canBoLapPhieuId;
    private String ldChiCuc;
    private Long ldChiCucId;
    private String ktvBaoQuan;
    private Long ktvBaoQuanId;
    private String keToanTruong;
    private Long keToanTruongId;
    private String nguoiGiaoHang;
    private String soCmt;
    private String ctyNguoiGh;
    private String diaChi;
    private LocalDate thoiGianGiaoNhan;
    private BigDecimal soLuongCanDc;
    private BigDecimal soBangKeCh;
    private Long bangKeChId;
    private String donViTinh;
    private BigDecimal tongSoLuong;
    private String tongSoLuongBc;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String thanhTienBc;
    private String ghiChu;
    private String trangThai;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private String type;

    @Transient
    List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbPhieuXuatKhoDtl> dcnbPhieuXuatKhoDtl = new ArrayList<>();
}
