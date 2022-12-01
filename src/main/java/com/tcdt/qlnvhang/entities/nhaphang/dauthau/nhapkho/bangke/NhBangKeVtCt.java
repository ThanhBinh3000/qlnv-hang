package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangke;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NH_BANG_KE_VT_CT")
@Data
@NoArgsConstructor
public class NhBangKeVtCt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANG_KE_VT_CT_SEQ")
    @SequenceGenerator(sequenceName = "BANG_KE_VT_CT_SEQ", allocationSize = 1, name = "BANG_KE_VT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "BANG_KE_VT_ID")
    private Long bangKeVtId;

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @Column(name = "SO_SERIAL")
    private String soSerial;

    @Column(name = "STT")
    private Integer stt;
}

