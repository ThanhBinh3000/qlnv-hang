package com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = XhPhieuKnghiemCluong.TABLE_NAME )
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhPhieuKnghiemCluong extends TrangThaiBaseEntity implements Serializable {
    public static final String TABLE_NAME = "XH_PHIEU_KNGHIEM_CLUONG";
    private static final long serialVersionUID = -1315211820556764708L;
  
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_KNGHIEM_CLUONG_SEQ")
//    @SequenceGenerator(sequenceName = "XH_PHIEU_KNGHIEM_CLUONG_SEQ", allocationSize = 1, name = "XH_PHIEU_KNGHIEM_CLUONG_SEQ")
    @Column(name = "ID")
    private Long id;

    private Integer nam;

    private String maDvi;

    private String maQhns;

    private Long idBbLayMau;

    private String soBbLayMau;

    private String soQdGiaoNvXh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayQdGiaoNvXh;

    private Long idQdGiaoNvXh;

    private String soPhieu;

    private Long idNguoiKiemNghiem;

    private Long idTruongPhong;

    private Long idThuKho;

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

    private String soBbXuatDocKho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayXuatDocKho;

    // Transient
    @Transient
    private String tenThuKho;

    @Transient
    private String tenTruongPhong;

    @Transient
    private String tenNguoiKiemNghiem;

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

    // Print preview

    @Transient
    private String ngayLayMauFormat;
    @Transient
    private String ngayKnghiemFormat;
    @Transient
    private String tenHthucBquan;

    @Transient
    private String ngay;

    @Transient
    private String thang;


    public String getNgayLayMauFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return Objects.isNull(ngayLayMau) ? null : formatter.format(ngayLayMau);
    }

    public String getNgayKnghiemFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return Objects.isNull(ngayKnghiem) ? null : formatter.format(ngayKnghiem);
    }

    public String getNgay() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getDate());
    }

    public String getThang() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getMonth()+1);
    }
}
