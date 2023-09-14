package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbPhieuXuatKhoHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbPhieuXuatKhoHdr extends BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_PHIEU_XUAT_KHO_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbPhieuXuatKhoHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbPhieuXuatKhoHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbPhieuXuatKhoHdr.TABLE_NAME + "_SEQ")
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
    private String soBangKeCh;
    private Long bangKeChId;
    private String soBangKeVt;
    private Long bangKeVtId;
    private String donViTinh;
    private BigDecimal tongSoLuong;
    private String tongSoLuongBc;
    private BigDecimal donGia;
    private BigDecimal thanhTien; // tổng kinh phí
    private String thanhTienBc;
    private String ghiChu;
    @Access(value=AccessType.PROPERTY)
    private String trangThai;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private String type;
    @Transient
    private String maDviCha;

    @Transient
    List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbPhieuXuatKhoDtl> dcnbPhieuXuatKhoDtl = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
    public void setMaDvi(String maDvi) {
        this.maDvi = maDvi;
        // get đơn vị cấp cha
        this.maDviCha = this.maDvi.substring(0, this.maDvi.length() - 2);
    }
}
