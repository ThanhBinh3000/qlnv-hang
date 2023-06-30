package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ScPhieuXuatKhoHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScPhieuXuatKhoHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_PHIEU_XUAT_KHO_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScPhieuXuatKhoHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScPhieuXuatKhoHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScPhieuXuatKhoHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String soPhieuXuatKho;
    private LocalDate ngayTaoPhieu;
    private LocalDate NgayXuatKho;
    private Integer no;
    private String co;
    private String soQdGiaoNv;
    private LocalDate ngayKyQdGiaoNv;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    @Column(name = "SO_PHIEU_KIEM_DINH_CL")
    private String soPhieuKiemDinhChatLuong;
    private String loaiHang;
    private String chungLoaiHang;
    private String thuKho;
    private String lanhDaoCc;
    private String ktvBaoQuan;
    private String keToanTruong;
    private String nguoiGiaoHang;
    private String soCmt;
    @Column(name = "DVI_GIAO_HANG")
    private String dviNguoiGiaoHang;
    private String diaChi;
    private LocalDate thoiGianGiaoNhan;
    private String soBangKeCanHang;
    @Column(name = "TONG_SL")
    private String tongSoLuong;
    private String tongSoTien;
    private String ghiChu;
    private String trangThai;

    @Transient
    List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<ScPhieuXuatKhoDtl> children = new ArrayList<>();

}
