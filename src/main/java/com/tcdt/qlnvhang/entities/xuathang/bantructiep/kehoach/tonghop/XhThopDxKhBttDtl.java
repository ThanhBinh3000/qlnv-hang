package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "XH_THOP_DX_KH_BTT_DTL")
@Data
public class XhThopDxKhBttDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_THOP_DX_KH_BTT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_THOP_DX_KH_BTT_DTL_SEQ  ")
    @SequenceGenerator(sequenceName = "XH_THOP_DX_KH_BTT_DTL_SEQ  ", allocationSize = 1, name = "XH_THOP_DX_KH_BTT_DTL_SEQ  ")

    private Long id;

    private Long idThopHdr;

    private Long idDxHdr;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String soDxuat;

    private LocalDate ngayPduyet;

    private String trichYeu;

    private Integer slDviTsan;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private BigDecimal tongSoLuong;
}
