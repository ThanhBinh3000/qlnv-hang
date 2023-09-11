package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKtChatLuongDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbPhieuKtChatLuongHdrReq {
    private Long id;

    private Integer nam;

    private String maDvi;

    private String maQhns;

    private Long qdDcId;

    private String soQdinhDc;

    private String soPhieu;

    private LocalDate ngayLapPhieu;

    private String nguoiKt;

    private Long nguoiKtId;

    private Long tpNguoiKtId;

    private String tpNguoiKt;

    private String maDiemKho;

    private String tenDiemKho;

    private String maNhaKho;

    private String tenNhaKho;

    private String maNganKho;

    private String tenNganKho;

    private String maLoKho;

    private String tenLoKho;

    private String tenThuKho;

    private Long thuKhoId;
    private String maDiemKhoXuat;

    private String tenDiemKhoXuat;

    private String maNhaKhoXuat;

    private String tenNhaKhoXuat;

    private String maNganKhoXuat;

    private String tenNganKhoXuat;

    private String maLoKhoXuat;

    private String tenLoKhoXuat;

    private String soBbLayMau;

    private Long bbLayMauId;

    private LocalDate ngayLayMau;

    private LocalDate ngayKiem;

    private String loaiVthh;

    private String tenLoaiVthh;

    private String cloaiVthh;

    private String tenCloaiVthh;

    private String hinhThucBq;

    private String danhGiaCamQuan;

    private String nhanXetKetLuan;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;

    private String trangThai;

    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private String lyDoTuChoi;

    private Boolean thayDoiThuKho;

    private LocalDate ngayDuyetTp;

    private Long nguoiDuyetTp;

    private Long bbTinhKhoId;

    private String soBbTinhKho;

    private LocalDate ngayXuatDocKho;

    private Long bbHaoDoiId;

    private String soBbHaoDoi;
    private String nguoiGiaoHang;
    private String soCmt;
    private String dVGiaoHang;
    private String diaChiDonViGiaoHang;
    private String bienSoXe;
    private BigDecimal slNhapTheoQd;
    private BigDecimal slNhapTheoKb;
    private BigDecimal slNhapTheoKt;
    private String soChungThuGiamDinh;
    private LocalDate ngayGiamDinh;
    private String toChucGiamDinh;

    private List<FileDinhKemReq> bienBanLayMauDinhKem = new ArrayList<>();

    private List<DcnbPhieuKtChatLuongDtl> dcnbPhieuKtChatLuongDtl = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
