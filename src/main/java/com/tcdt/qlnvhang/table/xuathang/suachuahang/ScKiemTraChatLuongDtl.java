package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = ScKiemTraChatLuongDtl.TABLE_NAME)
public class ScKiemTraChatLuongDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_KIEM_TRA_CL_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScKiemTraChatLuongDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScKiemTraChatLuongDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScKiemTraChatLuongDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    private Long idHdr;

    private Integer thuTuHt;

    private String tenChiTieu;

    private String mucYeuCauNhap;

    private String mucYeuCauNhapToiDa;

    private String mucYeuCauNhapToiThieu;

    private String ketQua;

    private String phuongPhap;

    private String danhGia;

}
