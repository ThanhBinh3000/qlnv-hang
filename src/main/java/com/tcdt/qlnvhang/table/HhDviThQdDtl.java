package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_DVI_TH_QD_DTL")
@Data
public class HhDviThQdDtl implements Serializable {
    private static final long serialVersionUID = 1392256483928416897L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DVI_TH_QD_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_DVI_TH_QD_DTL_SEQ", allocationSize = 1, name = "HH_DVI_TH_QD_DTL_SEQ")
    private Long id;

    private Integer stt;
    private String maKhoNganLo;
    private String tenKhoNganLo;
    private BigDecimal soLuong;
    private String thuKhoPhuTrach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hdr")
    @JsonBackReference
    private HhDviThuchienQdinh parent;
}
