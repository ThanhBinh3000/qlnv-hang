package com.tcdt.qlnvhang.entities.nhaphang.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NH_BANG_KE_CAN_HANG_LT")
public class QlBangKeCanHangLt extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = -628455991119242429L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANG_KE_CAN_HANG_LT_SEQ")
    @SequenceGenerator(sequenceName = "BANG_KE_CAN_HANG_LT_SEQ", allocationSize = 1, name = "BANG_KE_CAN_HANG_LT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId;

    @Column(name = "QL_PHIEU_NHAP_KHO_LT_ID")
    private Long qlPhieuNhapKhoLtId; // NH_PHIEU_NHAP_KHO

    @Column(name = "SO_BANG_KE")
    private String soBangKe;

    @Column(name = "THU_KHO")
    private String thuKho;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "MA_NGAN_LO")
    private String maNganLo;

    @Column(name = "NGAY_NHAP")
    private LocalDate ngayNhap;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "TEN_NGUOI_GIAO_HANG")
    private String tenNguoiGiaoHang;

    @Column(name = "DIA_CHI_NGUOI_GIAO")
    private String diaChiNguoiGiao;

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "DIA_DIEM")
    private String diaDiem;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "TONG_TRONG_LUONG_BAO_BI")
    private BigDecimal tongTrongLuongBaoBi;

    private Integer so;
    private Integer nam;
    private String loaiVthh;
    @Transient
    private String tenVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    @Transient
    private List<QlBangKeChCtLt> chiTiets = new ArrayList<>();
}
