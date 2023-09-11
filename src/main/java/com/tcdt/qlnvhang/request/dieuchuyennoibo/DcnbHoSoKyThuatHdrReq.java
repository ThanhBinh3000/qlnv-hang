package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbHoSoBienBanDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbHoSoTaiLieuDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbHoSoKyThuatHdrReq {
    private Long id;
    private String maDvi;
    private String tenDvi;
    private String soHoSoKyThuat;
    private String soBienBanLayMau;
    private Long bienBanLayMauId;
    private String soQdnh;
    private String tenCbthskt;
    private Long cbthsktId;
    private LocalDate ngayDuyetHskt;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String tenCbthsktKx;
    private Long cbthsktKxId;
    private LocalDate ngayKtKx;
    private String soBienBanLayMauKx;
    private Long soBienBanLayMauKxId;
    private Boolean kqKiemTra;
    private String nhapChiTietThongTin;
    private String trangThai;
    private String lyDoTuChoi;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String tenTrangThai;
    private List<DcnbHoSoTaiLieuDtl> danhSachHoSoTaiLieu = new ArrayList<>();
    private List<DcnbHoSoBienBanDtl> danhSachHoSoBienBan = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
