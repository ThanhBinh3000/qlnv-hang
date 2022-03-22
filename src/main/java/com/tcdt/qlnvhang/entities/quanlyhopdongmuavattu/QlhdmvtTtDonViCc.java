package com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QLHDMVT_TT_DON_VI_CC")
public class QlhdmvtTtDonViCc {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "TEN_DV_THUC_HIEN")
    private String tenDvThucHien;

    @Column(name = "DIA_CHI")
    private String diaChi;

    @Column(name = "MA_SO_THUE")
    private String maSoThue;

    @Column(name = "SO_DIEN_THOAI")
    private String soDienThoai;

    @Column(name = "SO_TAI_KHOAN")
    private String soTaiKhoan;

    @Column(name = "TEN_NGUOI_DAI_DIEN")
    private String tenNguoiDaiDien;

    @Column(name = "CHUC_VU")
    private String chucVu;

    @Column(name = "NGAY_TAO")
    private LocalDate ngayTao;

    @Column(name = "NGUOI_TAO_ID")
    private Long nguoiTaoId;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaySua;

    @Column(name = "NGUOI_SUA_ID")
    private Long nguoiSuaId;
    
}
