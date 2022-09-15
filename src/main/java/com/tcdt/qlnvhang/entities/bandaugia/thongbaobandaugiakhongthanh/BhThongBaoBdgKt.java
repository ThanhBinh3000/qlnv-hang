package com.tcdt.qlnvhang.entities.bandaugia.thongbaobandaugiakhongthanh;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = BhThongBaoBdgKt.TABLE_NAME)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BhThongBaoBdgKt extends TrangThaiBaseEntity implements Serializable {

    public static final String TABLE_NAME = "BH_THONG_BAO_BDG_KT";
    private static final long serialVersionUID = -2543642789249190419L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "THONG_BAO_BDG_KT_SEQ")
    @SequenceGenerator(sequenceName = "THONG_BAO_BDG_KT_SEQ", allocationSize = 1, name = "THONG_BAO_BDG_KT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "MA_THONG_BAO")
    private String maThongBao;

    @Column(name = "TRICH_YEU")
    private String trichYeu;

    @Column(name = "NGAY_KY")
    private LocalDate ngayKy;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "THONG_BAO_BDG_ID")
    private Long thongBaoBdgId;

    @Column(name = "DON_VI_THONG_BAO")
    private String donViThongBao;

    @Column(name = "NGAY_TO_CHUC")
    private LocalDate ngayToChuc;

    @Column(name = "NOI_DUNG")
    private String noiDung;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Transient
    private List<BhQdPheDuyetKhBdgThongTinTaiSan> cts = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
