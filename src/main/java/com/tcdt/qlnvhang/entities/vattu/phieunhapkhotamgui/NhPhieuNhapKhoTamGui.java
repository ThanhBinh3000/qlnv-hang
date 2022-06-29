package com.tcdt.qlnvhang.entities.vattu.phieunhapkhotamgui;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiCtReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Where;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = NhPhieuNhapKhoTamGui.TABLE_NAME)
@Data
@NoArgsConstructor
public class NhPhieuNhapKhoTamGui extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -4126804462700206222L;
    public static final String TABLE_NAME = "NH_PHIEU_NHAP_KHO_TAM_GUI";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_NHAP_KHO_TAM_GUI_SEQ")
    @SequenceGenerator(sequenceName = "PHIEU_NHAP_KHO_TAM_GUI_SEQ", allocationSize = 1, name = "PHIEU_NHAP_KHO_TAM_GUI_SEQ")
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "qdgnvnx_Id")
    private HhQdGiaoNvuNhapxuatHdr qdGiaoNvNhapXuat;

    @Column(name = "SO_PHIEU")
    private String soPhieu;

    @Column(name = "NGAY_NHAP_KHO")
    private LocalDate ngayNhapKho;

    @Column(name = "NO")
    private BigDecimal no;

    @Column(name = "CO")
    private BigDecimal co;

    @Column(name = "NGUOI_GIAO_HANG")
    private String nguoiGiaoHang;

    @Column(name = "THOI_GIAN_GIAO_NHAN_HANG")
    private LocalDate thoiGianGiaoNhanHang;

    @Column(name = "NGAY_TAO_PHIEU")
    private LocalDate ngayTaoPhieu;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "MA_NGAN_LO")
    private String maNganLo;

    @Column(name = "TONG_SO_LUONG")
    private BigDecimal tongSoLuong;

    @Column(name = "TONG_SO_TIEN")
    private BigDecimal tongSoTien;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    private Long nguoiGuiDuyetId;
    private LocalDate ngayGuiDuyet;
    private Long nguoiPduyetId;
    private LocalDate ngayPduyet;
    private String trangThai;
    private String lyDoTuChoi;
    private String maDvi;
    private String capDvi;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "phieuNkTg", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<NhPhieuNhapKhoTamGuiCt> chiTiets = new ArrayList<>();

    public void manageChiTiets(List<NhPhieuNhapKhoTamGuiCtReq> chiTietsRq) {

        List<NhPhieuNhapKhoTamGuiCt> chiTiets = new ArrayList<>();
        if (!CollectionUtils.isEmpty(chiTietsRq)) {
            chiTiets = chiTietsRq.stream()
                    .map(l -> {
                        NhPhieuNhapKhoTamGuiCt ct = new NhPhieuNhapKhoTamGuiCt();
                        BeanUtils.copyProperties(l, ct, "id");
                        return ct;
                    }).collect(Collectors.toList());
        }

        this.setChiTiets(chiTiets);
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + NhPhieuNhapKhoTamGui.TABLE_NAME + "'")
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
