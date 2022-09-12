package com.tcdt.qlnvhang.entities.xuathang.bbhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKhoCt;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = XhBienBanHaoDoiCt.TABLE_NAME)
@Data
public class XhBienBanHaoDoiCt {
    public static final String TABLE_NAME = "XH_BB_HAO_DOI_CT";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BB_HAO_DOI_CT_SEQ")
    @SequenceGenerator(sequenceName = "XH_BB_HAO_DOI_CT_SEQ", allocationSize = 1, name = "XH_BB_HAO_DOI_CT_SEQ")
    private Long id;
    private Long bbHaoDoiId;
    private String thoigianBaoquan;
    private double slBaoQuan;
    private double slHaoHut;
    private double DinhMucHaoHut;
}
