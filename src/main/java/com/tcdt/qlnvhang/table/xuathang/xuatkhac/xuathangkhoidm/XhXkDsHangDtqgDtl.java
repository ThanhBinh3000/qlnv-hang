package com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = XhXkDsHangDtqgDtl.TABLE_NAME)
public class XhXkDsHangDtqgDtl {

    public static final String TABLE_NAME = "XH_XK_DS_HANG_DTQG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_XK_DS_HANG_DTQG_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_XK_DS_HANG_DTQG_DTL_SEQ", allocationSize = 1, name = "XH_XK_DS_HANG_DTQG_DTL_SEQ")
    private Long id;
    private String ma;
    private String ten;
    private String maCha;
    private String maDviTinh;
    private String cap;
    private String loaiHang;
    private String dviQly;
    private Boolean isNgoaiDanhMuc;
    private Long idHdr;
    private String loaiHinhXuat;
    @Transient
    private List<XhXkDsHangDtqgDtl> children = new ArrayList<>();
    @Transient
    private String tenLoaiHinhXuat;
}
