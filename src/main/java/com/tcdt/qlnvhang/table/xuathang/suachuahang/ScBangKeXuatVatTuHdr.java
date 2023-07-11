package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTDtl;
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
@Table(name = ScBangKeXuatVatTuHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScBangKeXuatVatTuHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_BANG_KE_XUAT_VT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBangKeXuatVatTuHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScBangKeXuatVatTuHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBangKeXuatVatTuHdr.TABLE_NAME + "_SEQ")
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
    private String lyDoTuChoi;
    @Transient
    private List<ScBangKeXuatVatTuDtl> children = new ArrayList<>();

}
