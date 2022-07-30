package com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NH_BB_BAN_GIAO_MAU_CT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BienBanBanGiaoMauCt implements Serializable {
    private static final long serialVersionUID = -88699762892842688L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_BAN_GIAO_MAU_CT_SEQ")
    @SequenceGenerator(sequenceName = "BB_BAN_GIAO_MAU_CT_SEQ", allocationSize = 1, name = "BB_BAN_GIAO_MAU_CT_SEQ")
    private Long id;
    private Long bbBanGiaoMauId;
    private String loaiDaiDien;
    private String daiDien;
}
