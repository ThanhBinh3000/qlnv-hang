package com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NH_PHIEU_NHAP_KHO_CT1")
public class NhPhieuNhapKhoCt1 implements Serializable {

    private static final long serialVersionUID = 3529822360093876437L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_NHAP_KHO_CT1_SEQ")
    @SequenceGenerator(sequenceName = "PHIEU_NHAP_KHO_CT1_SEQ", allocationSize = 1, name = "PHIEU_NHAP_KHO_CT1_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "PHIEU_NK_ID")
    private Long phieuNkId;

    @Column(name = "PHIEU_KTCL_ID")
    private Long phieuKtclId;

    @Transient
    private String soPhieuKtCl;
}
