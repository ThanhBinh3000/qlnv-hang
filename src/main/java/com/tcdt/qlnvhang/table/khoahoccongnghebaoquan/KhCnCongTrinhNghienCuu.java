package com.tcdt.qlnvhang.table.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "KH_CN_CONG_TRINH_NGHIEN_CUU")
@Data
public class KhCnCongTrinhNghienCuu extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "KH_CN_CONG_TRINH_NGHIEN_CUU";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KH_CN_CONG_TRINH_NC_SEQ")
    @SequenceGenerator(sequenceName = "KH_CN_CONG_TRINH_NC_SEQ", allocationSize = 1, name = "KH_CN_CONG_TRINH_NC_SEQ")

    private Long id;
    private String nam;
    private String maDeTai;
    private String tenDeTai;
    private String capDeTai;
    @Temporal(TemporalType.DATE)
    private Date ngayKyTu;
    @Temporal(TemporalType.DATE)
    private Date ngayKyDen;
    private String chuNhiem;
    private String chucVu;
    private String email;
    private String sdt;
    private String dviChuTri;
    private String dviPhoiHop;
    private String dviThucHien;
    private String nguonVon;
    private String soQdPd;
    private String suCanThiet;
    private String mucTieu;
    private String phamVi;
    private String noiDung;
    private String phuongPhap;
    private BigDecimal tongChiPhi;
    @Temporal(TemporalType.DATE)
    private Date ngayGduyet;
    private Long nguoiGduyetId;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private Long nguoiPduyetId;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String maDvi;
    @Transient
    private String tenDvi;
    @Temporal(TemporalType.DATE)
    private Date ngayNghiemThu;
    private String diaDiem;
    private String danhGia;
    private Integer tongDiem;
    private String xepLoai;
    private String ldoTuChoi;
    @Transient
    private String tenXepLoai;
    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileTienDoTh = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileNghiemThuTl = new ArrayList<>();
    @Transient
    private List<KhCnTienDoThucHien> tienDoThucHien = new ArrayList<>();
    @Transient
    private List<KhCnNghiemThuThanhLy> children = new ArrayList<>();

    public String getTenXepLoai() {
        if (!ObjectUtils.isEmpty(tongDiem)) {
            if (tongDiem < 60) {
                tenXepLoai = "Không nghiệm thu";
            } else if (tongDiem >= 60 && tongDiem <= 69) {
                tenXepLoai = "Đạt yêu cầu";
            } else if (tongDiem >= 70 && tongDiem <= 79) {
                tenXepLoai = "Khá";
            } else if (tongDiem >= 80 && tongDiem <= 89) {
                tenXepLoai = "Giỏi";
            } else if (tongDiem >= 90 && tongDiem <= 100) {
                tenXepLoai = "Xuất sắc";
            }
            return tenXepLoai;
        } else
            return "";
    }

    //dùng cho preview
    @Transient
    private String tenCapDeTai;
    @Transient
    private String tenNguonVon;
    @Transient
    private String tongChiPhiStr;
}
