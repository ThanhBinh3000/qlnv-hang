package com.tcdt.qlnvhang.entities.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

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
    private String soBienBan;
    private String maDiemkho;
    private String maNhakho;
    private String maNgankho;
    private String maLokho;
    private String maLoaiHangHoa;
    private String maChungLoaiHangHoa;
    private double soLuongNhap;
    private double soLuongXuat;
    private double slConlaiSosach;
    private double slConlaiXuatcuoi;
    private double slThuaConlai;
    private double slThieuConlai;
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
