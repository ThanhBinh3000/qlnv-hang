package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = XhThopDxKhBdg.TABLE_NAME)
@Data
public class XhThopDxKhBdg implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_THOP_DX_KH_BAN_DAU_GIA";
    @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_THOP_DX_KH_BDG_SEQ")
//  @SequenceGenerator(sequenceName = "XH_THOP_DX_KH_BDG_SEQ", allocationSize = 1, name = "XH_THOP_DX_KH_BDG_SEQ")
    private Long id;
    private LocalDate ngayThop;
    private String loaiVthh;
    private String cloaiVthh;
    private LocalDate ngayDuyetTu;
    private LocalDate ngayDuyetDen;
    private String noiDungThop;
    private Integer namKh;
    private String maDvi;
    private Long idQdPd;
    private String soQdPd;
    private String trangThai;
    private LocalDate ngayTao;
    private Long nguoiTaoId;
    private LocalDate ngaySua;
    private Long nguoiSuaId;
    private LocalDate ngayGuiDuyet;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;

    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucVthh;

    public void setMapDmucVthh(Map<String, String> mapDmucVthh) {
        boolean isNewValue = !Objects.equals(this.mapDmucVthh, mapDmucVthh);
        this.mapDmucVthh = mapDmucVthh;
        if (isNewValue && !DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapDmucVthh.getOrDefault(getLoaiVthh(), null));
        }
        if (isNewValue && !DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapDmucVthh.getOrDefault(getCloaiVthh(), null));
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(mappedBy = "tongHopHdr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<XhThopDxKhBdgDtl> children = new ArrayList<>();

    public void setChildren(List<XhThopDxKhBdgDtl> children) {
        this.getChildren().clear();
        if (children != null) {
            children.forEach(child -> child.setTongHopHdr(this));
            this.children.addAll(children);
        }
    }

    @Transient
    private List<XhDxKhBanDauGia> xhDxKhBanDauGia;
}