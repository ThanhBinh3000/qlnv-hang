package com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.bangkethumuale;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = BangKeMuaLe.TABLE_NAME)
public class BangKeMuaLe extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_BANG_KE_MUA_LE";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;
    private String soBangKe;
    private String maDvi;
    @Transient
    private String tenDvi;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    private BigDecimal soLuongQd;
    private BigDecimal soLuongConLai;
    private Integer namQd;
    private String nguoiMua;
    private String diaChiThuMua;
    private LocalDate ngayMua;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private BigDecimal soLuongMtt;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String nguoiBan;
    private String diaChiNguoiBan;
    private String soCmt;
    private String ghiChu;
    private String nguoiTao;

}
