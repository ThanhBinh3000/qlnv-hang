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
    private String tenDvi;

    private String diaChiDvi;

    private String soDxuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;

    private String trichYeu;

    private String tenDuAn;

    private BigDecimal tongSoLuong;

    private Integer namKh;

    private BigDecimal tongTienVat;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String ptMua;

    private String tchuanCluong;

    private String giaMua;

    private BigDecimal donGia;

    private BigDecimal thueGtgt;

    private BigDecimal donGiaVat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKthuc;

    private String ghiChu;

    private String nguonVon;

    private BigDecimal tongMucDt;


}
