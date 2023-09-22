package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
@Table(name = "NH_BANG_KE_CAN_HANG")
public class NhBangKeCanHang extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = -628455991119242429L;

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANG_KE_CAN_HANG_LT_SEQ")
//    @SequenceGenerator(sequenceName = "BANG_KE_CAN_HANG_LT_SEQ", allocationSize = 1, name = "BANG_KE_CAN_HANG_LT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "SO_BANG_KE")
    private String soBangKe;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "ID_DDIEM_GIAO_NV_NH")
    private Long idDdiemGiaoNvNh;

    @Column(name ="SO_PHIEU_NHAP_KHO")
    private String soPhieuNhapKho;

    @Column(name = "MA_DVI")
    private String maDvi;
    private String maQhns;
    private String nguoiGiamSat;
    private BigDecimal trongLuongBaoBi;
    @Transient
    private String tenDvi;

    @Column(name = "NAM")
    private Integer nam;
    @Transient
    private List<FileDinhKem> listFileDinhKem;
    @Transient
    private List<NhBangKeCanHangCt> chiTiets = new ArrayList<>();
}
