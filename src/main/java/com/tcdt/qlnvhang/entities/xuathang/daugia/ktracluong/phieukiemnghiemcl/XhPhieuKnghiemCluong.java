package com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "XH_PHIEU_KNGHIEM_CLUONG")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhPhieuKnghiemCluong extends TrangThaiBaseEntity implements Serializable {

    private static final long serialVersionUID = -1315211820556764708L;

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_KNGHIEM_CLUONG_SEQ")
//    @SequenceGenerator(sequenceName = "XH_PHIEU_KNGHIEM_CLUONG_SEQ", allocationSize = 1, name = "XH_PHIEU_KNGHIEM_CLUONG_SEQ")
    @Column(name = "ID")
    private Long id;

    private Integer nam;

    private String maDvi;

    private String maQhns;

    private String soBbLayMau;

    private String soQdGiaoNvXh;

    private Long idQdGiaoNvXh;

    private String soPhieu;

    private Long idNguoiKiemNghiem;

    private Long idTruongPhong;

    private Long idKtv;

    private Long idDdiemXh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String hthucBquan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLayMau;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKnghiem;

    private String ketQua;

    private String ketLuan;

    // Transient
    @Transient
    private String tenKtv;

    @Transient
    private String tenDvi;

    @Transient
    private String tenLoaiVthh;

    @Transient
    private String tenCloaiVthh;

    @Transient
    private String tenDiemKho;

    @Transient
    private String tenNhaKho;

    @Transient
    private String tenNganKho;

    @Transient
    private String tenLoKho;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<XhPhieuKnghiemCluongCt> children = new ArrayList<>();
}
