package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "NH_BIEN_BAN_CHUAN_BI_KHO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NhBienBanChuanBiKho extends TrangThaiBaseEntity implements Serializable {

    private static final long serialVersionUID = 1117405412018192929L;
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BIEN_BAN_CHUAN_BI_KHO_SEQ")
//    @SequenceGenerator(sequenceName = "BIEN_BAN_CHUAN_BI_KHO_SEQ", allocationSize = 1, name = "BIEN_BAN_CHUAN_BI_KHO_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh;

    @Column(name = "SO_BIEN_BAN")
    private String soBienBan;

    @Column(name = "NGAY_NGHIEM_THU")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNghiemThu;

    @Column(name = "ID_KY_THUAT_VIEN")
    private Long idKyThuatVien;

    @Transient
    private String tenKyThuatVien;

    @Column(name = "ID_THU_KHO")
    private Long idThuKho;

    @Transient
    private String tenThuKho;

    @Column(name = "LOAI_HINH_KHO")
    private String loaiHinhKho;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Transient
    private String tenDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Transient
    private String tenNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Transient
    private String tenNganKho;

    @Column(name = "MA_LO_KHO")
    private String maLoKho;

    @Transient
    private String tenLoKho;

    @Column(name = "PTHUC_BQUAN")
    private String pthucBquan;

    @Column(name = "THUC_NHAP")
    private String thucNhap;

    @Column(name = "HTHUC_BQUAN")
    private String hthucBquan;

    @Column(name = "KET_LUAN")
    private String ketLuan;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Transient
    private String tenDvi;

    @Column(name = "TONG_SO")
    private BigDecimal tongSo;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Transient
    String tenLoaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;
    private String tenKeToan;

    @Transient
    private String tenCloaiVthh;

    @Column(name = "SO_LUONG_DDIEM_GIAO_NV_NH")
    private BigDecimal soLuongDdiemGiaoNvNh;

    @Column(name = "ID_DDIEM_GIAO_NV_NH")
    private Long idDdiemGiaoNvNh;

    @Transient
    private List<NhBienBanChuanBiKhoCt> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
