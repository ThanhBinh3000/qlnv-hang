package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaDiaDiemGiaoNhan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "HH_QD_PHE_DUYET_KHMTT_DX")
@Data

public class HhQdPheduyetKhMttDx implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PHE_DUYET_KHMTT_DX_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PHE_DUYET_KHMTT_DX_SEQ", allocationSize = 1, name = "HH_QD_PHE_DUYET_KHMTT_DX_SEQ")

    private Long id;

    private Long idQdKhmtt;

    private String maDvi;

    private String tenDvi;

    private String diaChi;

    private String soKhoach;

    @Temporal(TemporalType.DATE)
    private Date ngayKy;

    private  String trichYeu;

    private  String tenDuAn;

    private BigDecimal soLuong;

    private BigDecimal tongTien;

}
