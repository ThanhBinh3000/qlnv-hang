package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "NH_HO_SO_BIEN_BAN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhHoSoBienBan extends TrangThaiBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "NH_HO_SO_BIEN_BAN";

    @Id
    private Long id;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "SO_BIEN_BAN ")
    private String soBienBan;

    @Column(name = "LOAI_BB")
    private String loaiBb;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "SO_HO_SO_KY_THUAT")
    private String soHoSoKyThuat;

    @Column(name = "SO_BB_LAY_MAU")
    private String soBbLayMau;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh;

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh;

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "NGAY_HD")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    @Column(name = "SO_BIEN_BAN_GUI_HANG")
    private String soBienBanGuiHang;

    @Column(name = "DIA_DIEM_KIEM_TRA")
    private String diaDiemKiemTra;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Transient
    private String tenLoaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Transient
    private String tenCloaiVthh;

    @Column(name = "NOI_DUNG")
    private String noiDung;

    @Column(name = "SO_SERIAL")
    private String soSerial;

    @Column(name = "KY_MA_HIEU")
    private String kyMaHieu;

    @Column(name = "PP_LAY_MAU")
    private String ppLayMau;

    @Column(name = "SO_LUONG_NHAP")
    private BigDecimal soLuongNhap;

    @Column(name = "TGIAN_NHAP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianNhap;

    @Column(name = "TGIAN_BSUNG")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianBsung;

    @Column(name = "TGIAN_KTRA")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKtra;

    @Column(name = "KET_LUAN")
    private String ketLuan;

    @Transient
    private String tenDvi;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<NhHoSoBienBanCt> children = new ArrayList<>();

    @Transient
    private String tenBb;

    public String getTenBb() {
        if(tenBb != null){
            switch (tenBb){
                case "BBKTNQ" :
                    return "Biên bản kiểm tra ngoại quan";
                case "BBKTVH" :
                    return "Biên bản kiểm tra vận hành";
                case "BBKTHSKT" :
                    return "Biên bản kiểm tra hồ sơ kỹ thuật\t";
                default:
                    return null;
            }
        }
        return tenBb;
    }
}
