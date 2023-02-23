package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtvtDeXuatHdr.TABLE_NAME)
@Data
public class XhCtvtDeXuatHdr extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_CTVT_DE_XUAT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtDeXuatHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhCtvtDeXuatHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhCtvtDeXuatHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String loaiNhapXuat;
    private String kieuNhapXuat;
    private String soDx;
    private String trichYeu;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenVthh;
    private BigDecimal tonKho;
    private LocalDate ngayDx;
    private LocalDate ngayKetThuc;
    private String noiDungDx;
    private String trangThai;
    private Long idThop;
    private String maTongHop;
    private Long idQdPd;
    private String soQdPd;
    private LocalDate ngayKyQd;
    private BigDecimal tongSoLuong;
    private BigDecimal soLuongXuatCap;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private String type;
    private BigDecimal thanhTien;
    @Transient
    private String tenDvi;
    @Transient
    private String diaChiDvi;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenLoaiHinhNhapXuat;
    @Transient
    private String tenTrangThaiTh;
    @Transient
    private String tenTrangThaiQd;
    @Transient
    private List<FileDinhKem> canCu = new ArrayList<>();

    @Transient
    private List<XhCtvtDeXuatPa> deXuatPhuongAn = new ArrayList<>();

}
