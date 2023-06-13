package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
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

    private Long idThopHdr;

    private Long idDxHdr;

    private String maDvi;

    private String soDxuat;

    private LocalDate ngayPduyet;

    private String trichYeu;

    private Integer slDviTsan;
    private String trangThai;

    private BigDecimal tongSoLuong;

    private BigDecimal khoanTienDatTruoc;

    @Transient
    private BigDecimal tongTienGiaKdTheoDgiaDd;

    @Transient
    private BigDecimal tongKhoanTienDtTheoDgiaDd;

    // Transient
    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;

    public String getTenTrangThai() {
        return NhapXuatHangTrangThaiEnum.getTenById(trangThai);
    }
}
