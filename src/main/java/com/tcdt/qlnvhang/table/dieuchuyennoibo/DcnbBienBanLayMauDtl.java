package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBienBanLayMauDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbBienBanLayMauDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BIEN_BAN_LAY_MAU_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ")
    private Long id;

    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;

    @Column(name = "LOAI_DAI_DIEN")
    private String loaiDaiDien;

    @Column(name = "DAI_DIEN")
    private String daiDienCuc;

    @Column(name = "DAI_DIEN_CHI_CUC")
    private String daiDienChiCuc;

    @Column(name = "SL_MAU_HANG_KIEM_TRA")
    private Long slMauHangKiemTra;

    @Column(name = "PHUONG_PHAP_LAY_MAU")
    private String phuongPhapLayMau;

    @Column(name = "CHI_TIEU_KIEM_TRA")
    private String chiTieuKiemTra;

    @Column(name = "DA_NIEM_PHONG_MAU")
    private Boolean daNiemPhongMau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBienBanLayMauHdr dcnbBienBanLayMauHdr;

    @Transient
    private List<FileDinhKem> fileDinhKemChupMauNiemPhong = new ArrayList<>();


}
