package com.tcdt.qlnvhang.entities.xuathang.phieukiemnghiemchatluong;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_PHIEU_KNGHIEM_CLUONG")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhPhieuKnghiemCluong extends TrangThaiBaseEntity implements Serializable {

    private static final long serialVersionUID = -1315211820556764708L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_KNGHIEM_CLUONG_SEQ")
    @SequenceGenerator(sequenceName = "XH_PHIEU_KNGHIEM_CLUONG_SEQ", allocationSize = 1, name = "XH_PHIEU_KNGHIEM_CLUONG_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "SO_PHIEU")
    private String soPhieu;

    @Column(name = "NGAY_LAY_MAU")
    private LocalDate ngayLayMau;

    @Column(name = "NGAY_KNGHIEM")
    private LocalDate ngayKnghiem;

    @Column(name = "MA_NGAN_LO")
    private String maNganLo;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "NGAY_NHAP_DAY")
    private LocalDate ngayNhapDay;

    @Column(name = "SLUONG_BQUAN")
    private BigDecimal sluongBquan;

    @Column(name = "HTHUC_BQUAN")
    private String hthucBquan;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "THU_KHO")
    private String thuKho;

    @Column(name = "KET_LUAN")
    private String ketLuan;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "QDGNVX_ID")
    private Long qdgnvxId;

    @Column(name = "KET_QUA_DANH_GIA")
    private String ketQuaDanhGia;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "XH_BB_BAN_LAY_MAU_ID")
    private Long xhBbBanLayMauId;

    @Column(name = "SO")
    private Integer so;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Transient
    private List<XhPhieuKnghiemCluongCt> cts = new ArrayList<>();
}
