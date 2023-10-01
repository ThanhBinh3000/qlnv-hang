package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhTlToChucNlq.TABLE_NAME)
@Data
public class XhTlToChucNlq implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_TO_CHUC_NLQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlToChucNlq.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlToChucNlq.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlToChucNlq.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String hoVaTen;
    private String soCccd;
    private String chucVu;
    private String diaChi;
    private String loai;  //KM-khach moi    DGV-dau gia vien    NTG-nguoi tham gia

}
