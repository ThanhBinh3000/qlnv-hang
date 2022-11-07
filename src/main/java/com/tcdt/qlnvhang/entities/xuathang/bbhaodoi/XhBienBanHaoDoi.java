package com.tcdt.qlnvhang.entities.xuathang.bbhaodoi;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKho;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = XhBienBanHaoDoi.TABLE_NAME)
@EqualsAndHashCode(callSuper = true)
@Data
public class XhBienBanHaoDoi extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1036129798471529685L;
    public static final String TABLE_NAME = "XH_BB_HAO_DOI";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BB_HAO_DOI_SEQ")
    @SequenceGenerator(sequenceName = "XH_BB_HAO_DOI_SEQ", allocationSize = 1, name = "XH_BB_HAO_DOI_SEQ")
    private Long id;
    private Long bbTinhkhoId;
    private String soBienBan;
    private Date ngayNhap;
    private Date ngayXuat;
    private double slHaoThanhly;
    private double slHaoThucte;
    private double tileThucte;
    private double tileThanhly;
    private double soLuongNhap;
    private double soLuongXuat;
    private double slVuotDm;
    private double tileVuotDm;
    private double slDuoiDm;
    private double tileDuoiDm;
    private String maDvi;
    private String capDvi;
    private String nguyenNhan;
    private String kienNghi;
    private Long nguoiGuiDuyetId;
    private Long nguoiPduyetId;
    private String trangThai;
    private String lyDoTuChoi;
    private Integer so;
    private Integer nam;
}
