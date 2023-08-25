package com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhTcTtinBdgNlq.TABLE_NAME)
@Data
public class XhTcTtinBdgNlq implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TC_TTIN_BDG_NLQ";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTcTtinBdgNlq.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTcTtinBdgNlq.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTcTtinBdgNlq.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String hoVaTen;
    private String soCccd;
    private String chucVu;
    private String diaChi;
    private String loai;  //KM-khach moi    DGV-dau gia vien    NTG-nguoi tham gia
    private BigDecimal idVirtual;
}
