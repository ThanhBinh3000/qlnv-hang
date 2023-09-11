package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    private Integer nam;

    private String maDvi;

    private String tenDvi;

    private Long qhnsId;

    private String maQhns;

    private Long qdccId;

    private String soQdinhDcc;

    private String ktvBaoQuan;

    private Long ktvBaoQuanId;

    private String soBbLayMau;

    private LocalDate ngayLayMau;

    private String dViKiemNghiem;

    private String diaDiemLayMau;

    private String loaiVthh;

    private String tenLoaiVthh;

    private String cloaiVthh;

    private String tenCloaiVthh;

    private String maDiemKho;

    private String tenDiemKho;

    private String maNhaKho;

    private String tenNhaKho;

    private String maNganKho;

    private String tenNganKho;

    private String maLoKho;

    private String tenLoKho;
    private Long thuKho;
    private String tenThuKho;
    private String donViTinh;
    private String tenDonViTinh;
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

    private List<FileDinhKemReq> canCu = new ArrayList<>();
    private List<FileDinhKemReq> bienBanLayMauDinhKem = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKemChupMauNiemPhong = new ArrayList<>();
    private List<DcnbBienBanLayMauDtl> dcnbBienBanLayMauDtl = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
