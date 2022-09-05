package com.tcdt.qlnvhang.entities.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhBienBanTinhKho.TABLE_NAME)
@EqualsAndHashCode(callSuper = true)
@Data
public class XhBienBanTinhKho extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1036129798681529685L;
    public static final String TABLE_NAME = "XH_BB_TINH_KHO";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BB_TINH_KHO_SEQ")
    @SequenceGenerator(sequenceName = "XH_BB_TINH_KHO_SEQ", allocationSize = 1, name = "XH_BB_TINH_KHO_SEQ")
    private Long id;
    private Long qdgnvnxId;
    private String maDiemkho;
    private String maNhakho;
    private String maNgankho;
    private String maNganlo;
    private String maLoaiHangHoa;
    private String maChungLoaiHangHoa;
    private BigDecimal soLuongNhap;
    private BigDecimal soLuongXuat;
    private BigDecimal slConlaiSosach;
    private BigDecimal slConlaiXuatcuoi;
    private BigDecimal slThuaConlai;
    private BigDecimal slThieuConlai;
    private String maDvi;
    private String capDvi;
    private String nguyenNhan;
    private String kienNghi;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayGuiDuyet;
    private Long nguoiPduyetId;
    private LocalDate ngayPduyet;
    private String trangThai;
    private String lyDoTuChoi;
    private Integer so;
    private Integer nam;

}
