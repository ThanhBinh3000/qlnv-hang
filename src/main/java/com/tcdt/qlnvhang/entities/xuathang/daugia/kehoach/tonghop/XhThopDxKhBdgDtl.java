package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhThopDxKhBdgDtl.TABLE_NAME)
@Data
public class XhThopDxKhBdgDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_THOP_DX_KH_BDG_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThopDxKhBdgDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhThopDxKhBdgDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhThopDxKhBdgDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private String diaChi;
    private Long idDxHdr;
    private String soDxuat;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienKhoiDiemDx;
    private BigDecimal khoanTienDatTruoc;
    private BigDecimal tongTienDatTruocDx;
    private String trangThai;

    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHdr")
    @JsonIgnore
    private XhThopDxKhBdg tongHopHdr;
}
