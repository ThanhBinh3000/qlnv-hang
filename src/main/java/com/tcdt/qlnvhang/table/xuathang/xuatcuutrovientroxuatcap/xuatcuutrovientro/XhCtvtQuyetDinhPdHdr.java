package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtvtQuyetDinhPdHdr.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
public class XhCtvtQuyetDinhPdHdr extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_CTVT_QUYET_DINH_PD_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhCtvtQuyetDinhPdHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Integer nam;
    private String soBbQd;
    private LocalDate ngayKy;
    private LocalDate ngayHluc;
    private Long idTongHop;
    private String maTongHop;
    private LocalDate ngayThop;
    private Long idDx;
    private String soDx;
    private Long idXc;
    private String soXc;
    private LocalDate ngayDx;
    private Long tongSoLuongDx;
    private Long tongSoLuong;
    private Long thanhTien;
    private Long soLuongXuatCap;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenVthh;
    private String loaiNhapXuat;
    private String kieuNhapXuat;
    private String mucDichXuat;
    private String trichYeu;
    private String trangThai;
    private String lyDoTuChoi;
    private boolean xuatCap = false;
    private String type;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String donViTinh;
    @Column(name = "PA_XUAT_GAO_CHUYEN_XUAT_CAP")
    private Boolean paXuatGaoChuyenXc;
    @Column(name = "QD_PA_XUAT_CAP_ID")
    private Long qdPaXuatCapId;
  @Column(name = "SO_QD_PA_XUAT_CAP")
    private String qdPaXuatCap;
    private Long idQdGiaoNv;
    private String soQdGiaoNv;
    private LocalDate ngayKetThuc;

    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;

    @OneToMany(mappedBy = "xhCtvtQuyetDinhPdHdr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<XhCtvtQuyetDinhPdDtl> quyetDinhPdDtl = new ArrayList<>();

    public void setQuyetDinhPdDtl(List<XhCtvtQuyetDinhPdDtl> data) {
        quyetDinhPdDtl.clear();
        if (!DataUtils.isNullOrEmpty(data)) {
            data.forEach(s -> {
                s.setXhCtvtQuyetDinhPdHdr(this);
            });
            quyetDinhPdDtl.addAll(data);
        }
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_FILE_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_FILE_DINH_KEM");
                s.setXhCtvtQuyetDinhPdHdr(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU'")
    private List<FileDinhKemJoinTable> canCu = new ArrayList<>();

    public void setCanCu(List<FileDinhKemJoinTable> fileDinhKem) {
        this.canCu.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU");
                s.setXhCtvtQuyetDinhPdHdr(this);
            });
            this.canCu.addAll(fileDinhKem);
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }
}
