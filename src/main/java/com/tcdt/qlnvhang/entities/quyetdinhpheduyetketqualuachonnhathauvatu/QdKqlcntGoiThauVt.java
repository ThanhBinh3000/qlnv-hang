package com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "QD_KQLCNT_GOI_THAU_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QdKqlcntGoiThauVt implements Serializable {

    private static final long serialVersionUID = 7642261426428150783L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_KQLCNT_GOI_THAU_VT_SEQ")
    @SequenceGenerator(sequenceName = "QD_KQLCNT_GOI_THAU_VT_SEQ", allocationSize = 1, name = "QD_KQLCNT_GOI_THAU_VT_SEQ")
    private Long id;
    private String tenGoiThau;
    private BigDecimal soLuong;
    private BigDecimal giaGoiThau;
    private String trangThai;
    private String tenDonViTrungThau;
    private Long loaiHopDongId; //QLNV_DM
    private String thoiGianThucHien;
    private BigDecimal donGiaTruocThue;
    private BigDecimal thueVat;
    private BigDecimal donGiaSauThue;
    private BigDecimal donGiaHopDongTruocThue;
    private BigDecimal donGiaHopDongSauThue;
    private Long qdPdKhlcntId; //QD_PHE_DUYET_KQLCNT_VT
    private Long vatTuId; //QLNV_DM_VATTU
    private String maVatTu;
}
