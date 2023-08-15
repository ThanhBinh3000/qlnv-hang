package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.bienbanlaymau.XhBienBanLayMauDtl;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtvtTongHopHdr.TABLE_NAME)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class XhCtvtTongHopHdr extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_CTVT_TONG_HOP_HDR";

    @Id
    /*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtTongHopHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhCtvtTongHopHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhCtvtTongHopHdr.TABLE_NAME + "_SEQ")*/
    @NonNull
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maTongHop;
    private LocalDate ngayThop;
    private String noiDungThop;
    private String loaiNhapXuat;
    private String loaiVthh;
    private String cloaiVthh;
    private String trangThai;
    private Long idQdPd;
    private String soQdPd;
    private LocalDate ngayKyQd;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;

    private BigDecimal tongSlCtVt;
    private BigDecimal tongSlXuatCap;
    private BigDecimal tongSlDeXuat;


    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;

    @OneToMany(mappedBy = "xhCtvtTongHopHdr",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<XhCtvtTongHopDtl> deXuatCuuTro = new ArrayList<>();

    public void setXhCtvtTongHopDtl(List<XhCtvtTongHopDtl> xhCtvtTongHopDtl) {
        deXuatCuuTro.clear();
        if (!DataUtils.isNullOrEmpty(deXuatCuuTro)) {
            deXuatCuuTro.forEach(s -> {
                s.setXhCtvtTongHopHdr(this);
            });
            deXuatCuuTro.addAll(xhCtvtTongHopDtl);
        }
    }

}
