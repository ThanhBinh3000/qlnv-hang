package com.tcdt.qlnvhang.table.catalog;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "DM_VATTU_BQ")
@Data
public class QlnvDmVattuBq {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DM_VATTU_BQ_SEQ")
    @SequenceGenerator(sequenceName = "DM_VATTU_BQ_SEQ", allocationSize = 1, name = "DM_VATTU_BQ_SEQ")
    private Long id;

    String type;

    String ma;

    String giaTri;

    String maHangHoa;
}
