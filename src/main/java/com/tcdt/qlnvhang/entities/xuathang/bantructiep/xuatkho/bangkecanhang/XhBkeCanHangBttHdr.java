package com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = XhBkeCanHangBttHdr.TABLE_NAME)
@Data
public class XhBkeCanHangBttHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BKE_CAN_HANG_BTT_HDR";

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

    private String soBangKe;

    private Long idQd;

    private String soQd;

    private Long idHd;

    private String soHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyHd;

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

    private Long idPhieuXuat;

    private String soPhieuXuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayXuatKho;

    private String diaDiemKho;

    private Long idThuKho;
    @Transient
    private String tenThuKho;

    private String nguoiGiao;

    private String cmtNguoiGiao;

    private String ctyNguoiGiao;

    private String diaChiNguoiGiao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGiaoNhan;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String dviTinh;

    @Transient
    private List<XhBkeCanHangBttDtl> children = new ArrayList<>();
}
