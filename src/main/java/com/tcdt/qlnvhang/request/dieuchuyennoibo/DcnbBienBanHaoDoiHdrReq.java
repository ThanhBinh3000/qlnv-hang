package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiTtDtl;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DcnbBienBanHaoDoiHdrReq {
    private Long id;
    @NotNull
    private String loaiDc;

    private Boolean thayDoiThuKho;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String tenLoaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String tenCloaiVthh;
    @NotNull
    private Integer nam;

    private String maDvi;

    private String tenDvi;
    @NotNull
    private String maQhns;

    private String soBienBan;
    @NotNull
    private LocalDate ngayLap;
    @NotNull
    private String soQdinhDcc;
    @NotNull
    private Long qDinhDccId;
    @NotNull
    private LocalDate ngayKyQdDcc;
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
    @NotNull
    private String soBbTinhKho;
    @NotNull
    private Long bBTinhKhoId;
    @NotNull
    private LocalDate ngayBatDauXuat;
    @NotNull
    private LocalDate ngayKetThucXuat;
    private Double tongSlXuatTheoQd;

    private LocalDate ngayKetThucXuatQd;

    private Double tongSlXuatTheoTt;

    private LocalDate ngayKetThucXuatTt;

    private Double slHaoTt;

    private Double tiLeHaoTt;

    private Double slHaoVuotDm;

    private Double tiLeHaoVuotDm;

    private String nguyenNhan;

    private String kienNghi;

    private String ghiChu;

    private String thuKho;

    private LocalDate ngayPduyetKtvBQ;

    private String ktvBaoQuan;

    private Long ktvBaoQuanId;

    private LocalDate ngayPduyetKt;

    private String keToan;

    private Long keToanId;

    private LocalDate ngayPduyetLdcc;

    private String lanhDaoChiCuc;

    private Long lanhDaoChiCucId;

    private String trangThai;

    private LocalDate ngayGduyet;

    private Long nguoiGduyetId;

    private String lyDoTuChoi;
    private Long phieuKtChatLuongHdrId;
    private String soPhieuKtChatLuong;
    private String donViTinh;
    @Valid
    private List<DcnbBienBanHaoDoiTtDtl> danhSachBangKe = new ArrayList<>();
    @Valid
    private List<DcnbBienBanHaoDoiDtl> thongTinHaoHut = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
}
