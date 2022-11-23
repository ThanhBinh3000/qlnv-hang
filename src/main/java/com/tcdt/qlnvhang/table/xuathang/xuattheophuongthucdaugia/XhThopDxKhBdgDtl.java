package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "XH_THOP_DX_KH_BDG_DTL  ")
@Data
public class XhThopDxKhBdgDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_THOP_DX_KH_BDG_DTL  ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_THOP_DX_KH_BDG_DTL_SEQ  ")
    @SequenceGenerator(sequenceName = "XH_THOP_DX_KH_BDG_DTL_SEQ  ", allocationSize = 1, name = "XH_THOP_DX_KH_BDG_DTL_SEQ  ")
    private Long id;

    private Long idThopHdr;
    private Long idDxHdr;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String soDxuat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;
    private String trichYeu;
    private BigDecimal soLuong;
    private Integer soDviTsan;
    private BigDecimal tongTienKdiem;
    private BigDecimal tongTienDatTruoc;

}
