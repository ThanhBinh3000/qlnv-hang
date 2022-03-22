package com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QLHDMVT_DIA_DIEM_NHAP_VT")
public class QlhdmvtDiaDiemNhapVt {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "STT")
    private Long stt;

    @Column(name = "DON_VI_ID")
    private Long donViId;

    @Column(name = "MA_DON_VI")
    private String maDonVi;

    @Column(name = "SO_LUONG_NHAP")
    private Long soLuongNhap;

    @Column(name = "QLHDMVT_DS_GOI_THAU_ID")
    private Long qlhdmvtDsGoiThauId;
}
