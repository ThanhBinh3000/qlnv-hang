package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtVtQuyetDinhPdDtl.TABLE_NAME)
@Data
public class XhCtVtQuyetDinhPdDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_CTVT_QUYET_DINH_PD_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtVtQuyetDinhPdDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhCtVtQuyetDinhPdDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhCtVtQuyetDinhPdDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private Long idDx;
    private String soDx;
    private String maDviDx;
    private LocalDate ngayPduyetDx;
    private String trichYeuDx;
    private BigDecimal tongSoLuongDx;
    private BigDecimal soLuongXuatCap;
    private BigDecimal thanhTienDx;
    private LocalDate ngayDx;
    private LocalDate ngayKetThucDx;
    private String type;
    @Transient
    private String tenDviDx;
    @Transient
    private List<XhCtVtQuyetDinhPdDx> quyetDinhPdDx = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "idHdr", updatable = false, insertable = false)
    private XhCtVtQuyetDinhPdHdr XhCtVtQuyetDinhPdHdr;
}
