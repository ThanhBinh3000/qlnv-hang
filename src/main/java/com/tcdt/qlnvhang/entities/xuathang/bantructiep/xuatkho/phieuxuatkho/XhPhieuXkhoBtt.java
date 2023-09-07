package com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhPhieuXkhoBtt.TABLE_NAME)
@Data
public class XhPhieuXkhoBtt implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_PHIEU_XKHO_BTT";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =TABLE_NAME +"_SEQ")
//    @SequenceGenerator(sequenceName = TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")

    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String maQhns;

    private String soPhieuXuat;

    private LocalDate ngayXuatKho;

    private BigDecimal no;

    private BigDecimal co;

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

    private Long idPhieu;

    private String soPhieu;

    private LocalDate ngayKnghiem;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private Long idNguoiLapPhieu;
    @Transient
    private String tenNguoiLapPhieu;

    private Long idKtv;
    @Transient
    private String tenKtv;

    private String keToanTruong;

    private String nguoiGiao;

    private String cmtNguoiGiao;

    private String ctyNguoiGiao;

    private String diaChiNguoiGiao;

    private LocalDate tgianGiaoNhan;

    private Long idBangKe;

    private String soBangKe;

    private String maSo;

    private String donViTinh;

    private BigDecimal soLuongChungTu;

    private BigDecimal soLuongThucXuat;

    private BigDecimal donGia;

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
    @Transient
    private String tenNguoiPduyet;

    private String lyDoTuChoi;

    private String phanLoai;

    private String pthucBanTrucTiep;

    private Long idBangKeBanLe;

    private String soBangKeBanLe;

    private LocalDate ngayTaoBangKe;

    @Transient
    private String tenDviCungCap;
    @Transient
    private String tenDviCha;
    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
}
