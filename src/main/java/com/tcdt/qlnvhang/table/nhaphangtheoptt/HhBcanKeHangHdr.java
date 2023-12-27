package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_BCAN_KE_HANG_HDR")
@Data
public class HhBcanKeHangHdr  implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_BCAN_KE_HANG_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BCAN_KE_HANG_HDR_SEQ")
//    @SequenceGenerator(sequenceName = "HH_BCAN_KE_HANG_HDR_SEQ", allocationSize = 1, name = "HH_BCAN_KE_HANG_HDR_SEQ")

    private Long id;

    private Long idQdGiaoNvNh;

    private Long idPhieuNhapKho;

    private Long idDdiemGiaoNvNh;

    private Integer namKh;
    private String soBangKe;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String maQhns;

    private String soBangKeCanHang;

    private String soQuyetDinhNhap;

    private String soHdong;

    @Temporal(TemporalType.DATE)
    private Date ngayKiHdong;

    private String maDiemKho;
    @Transient
    private String tenDiemKho;

    private String maNhaKho;
    @Transient
    private String tenNhaKho;

    private String maNganKho;
    @Transient
    private String tenNganKho;

    private String maLoKho;
    @Transient
    private String tenLoKho;

    private String soPhieuNhapKho;

    @Temporal(TemporalType.DATE)
    private Date ngayNkho;

    private String diaDiemKho;

    private String hoTenNguoiGiao;

    private String cmt;

    private String donViGiao;

    private String diaChiNguoiGiao;

    @Temporal(TemporalType.DATE)
    private Date thoiGianGiaoNhan;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String donViTinh;

    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyet;

    @Temporal(TemporalType.DATE)
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private String lyDoTuChoi;

    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;

    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private String nguoiSua;

    private String soPhieuKtraCluong;
    private Long tongSlBaoBi;
    private Long tongSlCaBaoBi;
    private Long tongSlDaTruBaoBi;
    @Transient
    private String tongSlDaTruBaoBiBc;


    @Transient
    private List<HhBcanKeHangDtl> hhBcanKeHangDtlList = new ArrayList<>();





}
