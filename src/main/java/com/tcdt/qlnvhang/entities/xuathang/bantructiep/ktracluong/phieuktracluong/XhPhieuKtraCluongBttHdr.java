package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong;

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
@Table(name = "XH_PHIEU_KTRA_CLUONG_BTT_HDR")
@Data
public class XhPhieuKtraCluongBttHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_PHIEU_KTRA_CLUONG_BTT_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_KTRA_CLUONG_BTT_SEQ")
//    @SequenceGenerator(sequenceName = "XH_PHIEU_KTRA_CLUONG_BTT_SEQ", allocationSize = 1, name = "XH_PHIEU_KTRA_CLUONG_BTT_SEQ")
    @Column(name = "ID")
    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String maQhns;

    private Long idBienBan;

    private String soBienBan;

    private Long idQd;

    private String soQd;

    private String soPhieu;

    private Long idNgKnghiem;
    @Transient
    private String tenNguoiKiemNghiem;

    private Long idTruongPhong;
    @Transient
    private String tenTruongPhong;

    private Long idKtv;
    @Transient
    private String tenKtv;

    private Long idDdiemXh;

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

    private BigDecimal soLuong;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String hthucBquan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLayMau;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKnghiem;

    private String ketQua;

    private String ketLuan;

    @Transient
    private FileDinhKem fileDinhKem;

    @Transient
    private List<XhPhieuKtraCluongBttDtl> children = new ArrayList<>();


}
