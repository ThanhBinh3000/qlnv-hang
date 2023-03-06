package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.bangkebanle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "XH_BANG_KE_BTT")
@Data
public class XhBangKeBtt extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BANG_KE_BTT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BANG_KE_BTT_SEQ")
    @SequenceGenerator(sequenceName = "XH_BANG_KE_BTT_SEQ", allocationSize = 1, name = "XH_BANG_KE_BTT_SEQ")
    private Long id;

    private Integer namKh;

    private String soBangKe;

    private String soQd;

    private String maDvi;
    @Transient
    private String tenDvi;

    private BigDecimal soLuong;

    private BigDecimal soLuongConLai;

    private String nguoiPhuTrach;

    private String diaChi;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayBanHang;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private BigDecimal soLuongBtt;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String tenNguoiMua;

    private String diaChiNguoiMua;

    private String cmt;

    private String ghiChu;
}
