package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBKetThucNKDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DcnbBBKetThucNKReq extends BaseRequest {
    private Long id;
    private String loaiDc;
    private String typeQd;
    private String loaiQdinh;
    private Boolean thayDoiThuKho;
    private String loaiVthh;
    private String cloaiVthh;
    private Integer nam;
    private String soBb;
    private LocalDate ngayLap;
    private String maDvi;
    private Long qhnsId;
    private String maQhns;
    private Long qDinhDccId;
    private String soQdinhDcc;
    private String maDiemKho;
    private String tenDiemKho;
    private String diaDaDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private LocalDate ngayBatDauNhap;
    private LocalDate ngayKetThucNhap;
    private BigDecimal tongSlTheoQd;
    private String maLanhDaoChiCuc;
    private String tenLanhDaoChiCuc;
    private Long thuKhoId;
    private String tenThuKho;
    private Long ktvBQuan;
    private String tenKtvBQuan;
    private Long keToanTruong;
    private String tenKeToanTruong;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private Long nguoiPDuyetKtv;

    private LocalDate ngayPDuyetKtv;
    private Long nguoiPDuyetKt;

    private LocalDate ngayPDuyetKt;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;
    private String type;
    private List<DcnbBBKetThucNKDtl> dcnbBBKetThucNKDtl = new ArrayList<>();
    private LocalDate tuNgayKtnk;
    private LocalDate denNgayKtnk;
    private LocalDate tuNgayThoiHanNhap;
    private LocalDate denNgayThoiHanNhap;

    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
