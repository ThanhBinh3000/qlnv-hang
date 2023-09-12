package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauDtl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBienBanLayMauHdrReq implements Serializable {
    private Long id;

    private String loaiBb;
    private LocalDate thoiHanDieuChuyen;
    @NotNull
    private Integer nam;

    private String maDvi;

    private String tenDvi;

    private Long qhnsId;
    @NotNull
    private String maQhns;
    @NotNull
    private Long qdccId;

    private String soQdinhDcc;

    private String ktvBaoQuan;

    private Long ktvBaoQuanId;

    private String soBbLayMau;

    private LocalDate ngayLayMau;

    private String soBbNhapDayKho;
    private Long bBNhapDayKhoId;
    private LocalDate ngayNhapDayKho;

    private String soBbNtBqLd;
    private Long bbNtBqLdId;

    private String dViKiemNghiem;

    private String diaDiemLayMau;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String tenLoaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String tenCloaiVthh;
    @NotNull
    private String maDiemKho;
    @NotNull
    private String tenDiemKho;
    @NotNull
    private String maNhaKho;
    @NotNull
    private String tenNhaKho;
    @NotNull
    private String maNganKho;
    @NotNull
    private String tenNganKho;

    private String maLoKho;

    private String tenLoKho;
    private Long thuKho;
    private String tenThuKho;
    private String donViTinh;
    private Boolean thayDoiThuKho;

    private Long soLuongMau;

    private String pPLayMau;

    private String chiTieuKiemTra;

    private String trangThai;

    private String lyDoTuChoi;

    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;

    private String ketQuaNiemPhong;

    private String diaDiemBanGiao;

    private String loaiDc;

    private String type;

    private String soBbTinhKho;

    private Long bbTinhKhoId;

    private LocalDate ngayXuatDocKho;

    private String soBbHaoDoi;

    private Long bbHaoDoiId;
    private String ghiChu;

    private List<FileDinhKemReq> canCu = new ArrayList<>();
    private List<FileDinhKemReq> bienBanLayMauDinhKem = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKemChupMauNiemPhong = new ArrayList<>();
    private List<DcnbBienBanLayMauDtl> dcnbBienBanLayMauDtl = new ArrayList<>();
}
