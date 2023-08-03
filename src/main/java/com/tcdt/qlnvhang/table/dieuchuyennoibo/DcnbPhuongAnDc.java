package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = DcnbPhuongAnDc.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbPhuongAnDc implements Cloneable,Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_PHUONG_AN_DC";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbPhuongAnDc.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbPhuongAnDc.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbPhuongAnDc.TABLE_NAME + "_SEQ")
    private Long id;
    private Long parentId;
    private String maChiCucNhan;
    private String tenChiCucNhan;
    private String dviVanChuyen;
    private String tenDviVanChuyen;
    private String pthucGiaoHang;
    private String tenPthucGiaoHang;
    private String pthucNhanHang;
    private String tenPthucNhanHang;
    private String nguonChi;
    private String tenNguonChi;
    @Column(name = "TEN_HT_DC_CC_DV_VAN_CHUYEN")
    private String tenHinhThucDvCcDvVanChuyen;
    @Column(name = "HT_DC_CC_DV_VAN_CHUYEN")
    private String hinhThucDvCcDvVanChuyen;
    @Column(name = "KE_HOACH_DC_HDR_ID", insertable = true, updatable = true)
    private Long keHoachDcHdrId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KE_HOACH_DC_HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
