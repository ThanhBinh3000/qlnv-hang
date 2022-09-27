package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_DX_KHMTT_THOP_HDR")
@Data
public class HhDxKhMttThopHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_THOP_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_THOP_HDR_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_THOP_HDR_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_THOP_HDR_SEQ")
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date ngayThop;
    private String loaiVthh;
    @Transient
    String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    String tenCloaiVthh;
    private String moTaHangHoa;
    private Integer namKhoach;
    private  String noiDung;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;
    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private String nguoiSua;
    private String trangThai;
    private String SoQdPduyet;
    private BigDecimal tongMucDt;
    private BigDecimal tongSoLuong;
    private String maDvi;

    @Transient
    String tenTrangThai;

    @Transient
    List<HhDxKhMttThopDtl> hhDxKhMttThopDtls =new ArrayList<>();


}
