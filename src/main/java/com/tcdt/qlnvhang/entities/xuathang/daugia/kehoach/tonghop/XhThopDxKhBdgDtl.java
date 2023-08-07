package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "XH_THOP_DX_KH_BDG_DTL  ")
@Data
public class XhThopDxKhBdgDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_THOP_DX_KH_BDG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_THOP_DX_KH_BDG_DTL_SEQ  ")
    @SequenceGenerator(sequenceName = "XH_THOP_DX_KH_BDG_DTL_SEQ  ", allocationSize = 1, name = "XH_THOP_DX_KH_BDG_DTL_SEQ  ")

    private Long id;
    private Long idDxHdr;
    private String maDvi;
    private String soDxuat;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private BigDecimal donGiaDeXuat;
    private BigDecimal tongTienKhoiDiemDx;
    private BigDecimal khoanTienDatTruoc;
    private BigDecimal tongTienDatTruocDx;
    private String trangThai;

    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHdr")
    @JsonIgnore
    private XhThopDxKhBdg tongHopHdr;
}
