package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_PHIEU_KT_CHAT_LUONG")
@Data
public class HhPhieuKiemTraChatLuong implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_PHIEU_KT_CHAT_LUONG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_PHIEU_KT_CHAT_LUONG_SEQ")
    @SequenceGenerator(sequenceName = "HH_PHIEU_KT_CHAT_LUONG_SEQ", allocationSize = 1, name = "HH_PHIEU_KT_CHAT_LUONG_SEQ")
    private Long id;
    private Integer namKh;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String soPhieu;
    private String maQhns;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    @Temporal(TemporalType.DATE)
    private Date ngayQdGiaoNvNh;
    private String ktvBaoQuan;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String soHd;
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
    private String nguoiGiaoHang;
    private String cmtNguoiGiaoHang;
    private String donViGiaoHang;
    private String diaChi;
    private String bienSoXe;
    private Double soLuongDeNghiKt;
    private Double soLuongNhapKho;
    private Double slTtKtra;
    private Double slKhKb;
    private String soChungThuGiamDinh;
    @Temporal(TemporalType.DATE)
    private Date ngayGdinh;
    private String tchucGdinh;
    private String ketLuan;
    private String kqDanhGia;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private Long idDdiemGiaoNvNh;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;
    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private String nguoiSua;
    private String ldoTuChoi;
    @Temporal(TemporalType.DATE)
    private Date ngayGduyet;
    private String nguoiGduyet;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyet;
    @Transient
    private FileDinhKem fileDinhKem;
    @Transient
    private List<FileDinhKem> fileDinhKems =new ArrayList<>();
    @Transient
    private List<HhPhieuKiemTraChatLuongDtl> phieuKiemTraChatLuongDtlList= new ArrayList<>();

    @Transient
    private List<HhBcanKeHangHdr> bcanKeHangHdr;

    @Transient
    private List<HhPhieuNhapKhoHdr> phieuNhapKhoHdr;
    @Transient
    private Integer ngay;
    @Transient
    private Integer thang;
    @Transient
    private Integer nam;

}
