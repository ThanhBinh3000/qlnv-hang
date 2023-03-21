package com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = XhPhieuXkhoBtt.TABLE_NAME)
@Data
public class XhPhieuXkhoBtt extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_PHIEU_XKHO_BTT";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =TABLE_NAME +"_SEQ")
//    @SequenceGenerator(sequenceName = TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")
    @Column(name = "ID")
    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String maQhns;

    private String soPhieuXuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayXuatKho;

    private BigDecimal no;

    private BigDecimal co;

    private Long idQd;

    private String soQd;

    private Long idHd;

    private String soHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyHd;

    private Long  idDdiemXh;

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

    private Long idPhieuKtraCluong;

    private String soPhieu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKnghiem;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private Long idNguoiLapPhieu;
    @Transient
    private String tenNguoiLapPhieu;

    private Long idKtv;
    @Transient
    private String tenKtv;

    private String keToanTruong;

    private String nguoiGiao;

    private String cmtNguoiGiao;

    private String ctyNguoiGiao;

    private String diaChiNguoiGiao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGiaoNhan;

    private String soBangKe;

    private String  maSo;

    private String dviTinh;

    private BigDecimal soLuongChungTu;

    private BigDecimal soLuongThucXuat;

    private BigDecimal donGia;

    private String ghiChu;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

}
