package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NH_BB_LAY_MAU_CT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BienBanLayMauCt implements Serializable {
    private static final long serialVersionUID = 3098961232244402208L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_LAY_MAU_CT_SEQ")
    @SequenceGenerator(sequenceName = "BB_LAY_MAU_CT_SEQ", allocationSize = 1, name = "BB_LAY_MAU_CT_SEQ")
    private Long id;
    private Long bbLayMauId;
    private String loaiDaiDien;
    private String tenLoaiDaiDien;
    private String daiDien;
}
