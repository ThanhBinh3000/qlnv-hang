package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ScKiemTraChatLuongHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScKiemTraChatLuongHdr {
    public static final String TABLE_NAME = "SC_KIEM_TRA_CL_HDR";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScKiemTraChatLuongHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScKiemTraChatLuongHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScKiemTraChatLuongHdr.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "NAM")
    private Integer nam;
    @Column(name = "MA_DVI")
    private String maDvi;
    @Column(name = "TEN_DVI")
    private String tenDvi;
    @Column(name = "QHNS_ID")
    private Long qhnsId;
    @Column(name = "MA_QHNS")
    private String maQhns;
    @Column(name = "SO_PHIEU_KIEM_DINH_CL")
    private String soPhieuKdcl;
    private LocalDate ngayLap;
    private String canBoLapPhieuId;
    private String canBoLapPhieu;
    private String truongPhongKtbqId;
    private String truongPhongKtbq;
    private Long soQdGiaoNvId;
    private String soQdGiaoNv;
    private Long phieuXuatKhoId;
    private String soPhieuXuatKho;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String thuKhoId;
    private String thuKho;
    private String maLoaiHang;
    private String tenLoaiHang;
    private String maChungLoaiHang;
    private String tenChungLoaiHang;
    private String dviKiemDinh;
    private LocalDate ngayKiemDinh;
    private String hinhThucBaoQuan;
    private String trangThai;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<ScKiemTraChatLuongDtl> scKiemTraChatLuongDtls = new ArrayList<>();
}
