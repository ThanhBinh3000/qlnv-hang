package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiTtDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBienBanHaoDoiHdrReq {
    private Long id;

    private String loaiDc;

    private Boolean thayDoiThuKho;

    private String loaiVthh;

    private String tenLoaiVthh;

    private String cloaiVthh;

    private String tenCloaiVthh;

    private Integer nam;

    private String maDvi;

    private String tenDvi;

    private String maQhns;

    private String soBienBan;

    private LocalDate ngayLap;

    private String soQdinhDcc;

    private Long qDinhDccId;

    private LocalDate ngayKyQdDcc;

    private String maDiemKho;

    private String tenDiemKho;

    private String maNhaKho;

    private String tenNhaKho;

    private String maNganKho;

    private String tenNganKho;

    private String maLoKho;

    private String tenLoKho;

    private String soBbTinhKho;

    private Long bBTinhKhoId;

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

    private List<DcnbBienBanHaoDoiTtDtl> danhSachBangKe = new ArrayList<>();

    private List<DcnbBienBanHaoDoiDtl> thongTinHaoHut = new ArrayList<>();
}
