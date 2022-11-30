package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "HH_DX_KHMTT_THOP_DTL")
@Data
public class HhDxKhMttThopDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_THOP_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_THOP_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_THOP_DTL_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_THOP_DTL_SEQ")
    private Long id;
    private Long idThopHdr;
    private Long idDxHdr;
    private String maDvi;
    @Transient
    String tenDvi;
    private String soDxuat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private String tenDuAn;
    private BigDecimal soLuong;
    private String namKh;
    private String trichYeu;
    private String diaChiDvi;
    private BigDecimal donGiaVat;


}
