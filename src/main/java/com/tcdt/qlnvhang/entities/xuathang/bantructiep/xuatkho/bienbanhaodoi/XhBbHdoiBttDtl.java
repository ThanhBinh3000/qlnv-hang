package com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = XhBbHdoiBttDtl.TABLE_NAME)
@Data
public class XhBbHdoiBttDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BB_HDOI_BTT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =TABLE_NAME +"_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")

    @Column(name = "ID")
    private Long id;

    private Long idHdr;

    private String soPhieu;

    private String soPhieuXuat;

    private String soBangKe;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayXuatKho;

    private BigDecimal soLuongThucXuat;
}
