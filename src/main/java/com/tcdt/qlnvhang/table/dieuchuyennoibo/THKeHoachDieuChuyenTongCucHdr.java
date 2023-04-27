package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "DCNB_TH_KE_HOACH_DCTC_HDR")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class THKeHoachDieuChuyenTongCucHdr implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_TH_KH_DCTC_HDR_SEQ")
    @SequenceGenerator(sequenceName = "DCNB_TH_KH_DCTC_HDR_SEQ", allocationSize = 1, name = "DCNB_TH_KH_DCTC_HDR_SEQ")
    private Long id;

    @Column(name = "NGAY_TAO")
    private LocalDate ngaytao;

    @Column(name = "NGUOI_TAO_ID")
    private Long nguoiTaoId;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaySua;

    @Column(name = "NGUOI_SUA_ID")
    private Long nguoiSuaId;

    @Column(name = "MA_TONG_HOP")
    private String maTongHop;

    @Column(name = "NGAY_TONG_HOP")
    private LocalDate ngayTongHop;

    @Column(name = "NOI_DUNG")
    private String noiDung;

    @Column(name = "NAM_KE_HOACH")
    private Integer namKeHoach;

    @Column(name = "LOAI_DC")
    private String loaiDieuChuyen;

    @Column(name = "TH_TU_NGAY")
    private LocalDate thTuNgay;

    @Column(name = "TH_DEN_NGAY")
    private LocalDate thDenNgay;

    @Column(name = "LOAI_HH")
    private String loaiHangHoa;

    @Column(name = "TEN_LOAI_HH")
    private String tenLoaiHangHoa;

    @Column(name = "CLOAI_HH")
    private String chungLoaiHangHoa;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "MA_DVI")
    private String maDVi;

    @Column(name = "TEN_DVI")
    private String tenDVi;

    @Column(name = "THOI_GIAN_TONG_HOP")
    private Date thoiGianTongHop;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<THKeHoachDieuChuyenTongCucDtl> thKeHoachDieuChuyenNoiBoCucDtls = new ArrayList<>();
}
