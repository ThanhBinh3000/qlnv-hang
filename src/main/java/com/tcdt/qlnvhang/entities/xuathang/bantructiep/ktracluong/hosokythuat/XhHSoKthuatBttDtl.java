package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.hosokythuat;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhHSoKthuatBttDtl.TABLE_NAME)
@Data
public class XhHSoKthuatBttDtl  implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HSO_KTHUAT_BTT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =TABLE_NAME +"_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")
    private Long id;

    private Long idHdr;

    private String tenHoSo;

    private String loaiTaiLieu;

    private BigDecimal soLuong;

    private String ghiChu;

    @Transient
    private FileDinhKem fileDinhKems;
}
