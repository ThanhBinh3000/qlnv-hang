package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauHdr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ScBangKeNhapVtHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScBangKeNhapVtHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_BANG_KE_NHAP_VT_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBangKeNhapVtHdr.TABLE_NAME + "_SEQ")
//    @SequenceGenerator(sequenceName = ScBangKeNhapVtHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBangKeNhapVtHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String soBangKe;
    private LocalDate ngayNhap;
    private String soQdXh;
    private Long idQdXh;
    private String soPhieuXuatKho;
    private Long idPhieuXuatKho;
    private Long idThuKho;
    private Long idLanhDaoCc;
    private String lyDoTuChoi;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenLanhDaoCc;
    @Transient
    private List<ScBangKeNhapVtDtl> children = new ArrayList<>();
}
