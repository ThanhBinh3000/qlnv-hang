package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhThopDxKhBttDtl.TABLE_NAME)
@Data
public class XhThopDxKhBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_THOP_DX_KH_BTT_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThopDxKhBttDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhThopDxKhBttDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhThopDxKhBttDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Long idDxHdr;
    private String soDxuat;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String trangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHdr")
    @JsonIgnore
    private XhThopDxKhBttHdr tongHopHdr;
}
