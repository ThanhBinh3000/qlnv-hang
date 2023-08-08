package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NH_BIEN_BAN_CHUAN_BI_KHO_CT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhBienBanChuanBiKhoCt implements Serializable {

    private static final long serialVersionUID = -7498145675622743869L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_CHUAN_BI_KHO_CT_SEQ")
    @SequenceGenerator(sequenceName = "BB_CHUAN_BI_KHO_CT_SEQ", allocationSize = 1, name = "BB_CHUAN_BI_KHO_CT_SEQ")
    @Column(name = "ID")
    private Long id;
    private Long hdrId;
    private String danhMuc;
    private String nhomHang;
    private String donViTinh;
    private String matHang;
    private String tenMatHang;
    private String donViTinhMh;
    private Double tongGiaTri;
    private Double soLuongTrongNam;
    private Double donGia;
    private Double thanhTienTrongNam;
    private Double soLuongNamTruoc;
    private Double thanhTienNamTruoc;
    private String type;
    private Boolean isParent;
    private String idParent;
}
