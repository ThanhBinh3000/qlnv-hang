package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBBNTBQHdrReq extends BaseRequest {

    private Long id;
    @NotNull
    private String loaiDc;
    private Boolean thayDoiThuKho;
    private String loaiQdinh;
    private String typeQd;
    private String type;
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
    private String soBban;
    @NotNull
    private LocalDate ngayLap;
    private LocalDate ngayKetThucNt;
    @NotNull
    private String soQdDcCuc;
    @NotNull
    private Long qdDcCucId;
    @NotNull
    private LocalDate ngayQdDcCuc;
    private String thuKho;
    private String kthuatVien;
    private String keToan;
    private String ldChiCuc;
    private Long keHoachDcDtlId;
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
    private String loaiHinhKho;
    private Double tichLuongKhaDung;
    private String dsPhieuNhapKho;
    private Double slThucNhapDc;
    private String soBbNhapDayKho;
    private Long bbNhapDayKhoId;
    private String hinhThucBaoQuan;
    private String phuongThucBaoQuan;
    private Double dinhMucDuocGiao;
    private Double dinhMucTT;
    @NotNull
    private BigDecimal tongKinhPhiDaTh;
    @NotNull
    private String tongKinhPhiDaThBc;
    @NotNull
    private String maDiemKhoXuat;
    @NotNull
    private String tenDiemKhoXuat;
    @NotNull
    private String maNhaKhoXuat;
    @NotNull
    private String tenNhaKhoXuat;
    @NotNull
    private String maNganKhoXuat;
    @NotNull
    private String tenNganKhoXuat;
    private String maLoKhoXuat;
    private String tenLoKhoXuat;
    private String nhanXet;
    private String trangThai;
    private String lyDoTuChoi;
    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
    @NotNull
    private String donViTinh;
    @Valid
    private List<DcnbBBNTBQDtl> dcnbBBNTBQDtl = new ArrayList<>();
    private LocalDate tuNgayLap;
    private LocalDate denNgayLap;
    private LocalDate tuNgayKtnt;
    private LocalDate denNgayKtnt;
    private ReportTemplateRequest reportTemplateRequest;
    private String loaiHinhBaoQuan;
}
