package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhTlBangKeDtl.TABLE_NAME)
@Data
public class XhTlBangKeDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_BANG_KE_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlBangKeDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlBangKeDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlBangKeDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private String maCan;
    private Long soBaoBi;
    private Long trongLuongBaoBi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHdr")
    @JsonIgnore
    private XhTlBangKeHdr bangKeHdr;
}