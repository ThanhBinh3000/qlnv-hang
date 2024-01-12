package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = HhQdPduyetKqlcntDtl.TABLE_NAME)
@Data
public class HhQdPduyetKqlcntDtl implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PDUYET_KQLCNT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhQdPduyetKqlcntDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = HhQdPduyetKqlcntDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = HhQdPduyetKqlcntDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idGoiThau;
    private Long idQdPdHdr;
    private Long idNhaThau;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTienNhaThau;
    private String trangThai;
    private String type;
    private String tenNhaThau;
    private String dienGiaiNhaThau;
    @Transient
    private String tenTrangThai;
    public String getTenTrangThai() {
        return NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(this.getTrangThai());
    }
}
