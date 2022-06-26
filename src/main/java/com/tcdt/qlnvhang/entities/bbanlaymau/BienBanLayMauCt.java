package com.tcdt.qlnvhang.entities.bbanlaymau;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "NH_BB_LAY_MAU_CT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BienBanLayMauCt extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_LAY_MAU_CT_SEQ")
    @SequenceGenerator(sequenceName = "BB_LAY_MAU_CT_SEQ", allocationSize = 1, name = "BB_LAY_MAU_CT_SEQ")
    private Long id;
    private Long bbLayMauId;
    private String loaiDaiDien;
    private String daiDien;
}
