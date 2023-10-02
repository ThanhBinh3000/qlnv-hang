package com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt1;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhPhieuNhapKhoReq extends BaseRequest {

    private List<Long> phieuKtClIds = new ArrayList<>();
    private String soPhieu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLap;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
    private Date thoiGianGiaoNhan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date tuNgayNk;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date denNgayNk;
    private Long qdgnvnxId;

    private List<NhPhieuNhapKhoCtReq> hangHoaList = new ArrayList<>();

    private List<FileDinhKemReq> chungTus;

    private Long id;

    private String soPhieuKtraCl;

    private String soBienBanGuiHang;

    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    private String soPhieuNhapKho;

    private String soHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhapKho;

    private String nguoiGiaoHang;

    private Date ngayTaoPhieu;

    private BigDecimal taiKhoanNo;

    private BigDecimal taiKhoanCo;

    private String loaiHinhNhap;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String maDvi;

    private String maQhns;

    private Integer so;

    private Integer nam;

    // Vat tu
    private Long hoSoKyThuatId;

    private BigDecimal tongSoLuong;

    private BigDecimal tongSoTien;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private Integer soNo; // Số nợ

    private Integer soCo; // Số có

    private String cmtNguoiGiaoHang;

    private String donViGiaoHang;

    private String diaChiNguoiGiao;

    private String keToanTruong;

    private String ghiChu;

    private Long idDdiemGiaoNvNh;

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<FileDinhKemReq> chungTuKemTheo = new ArrayList<>();

    @Transient
    private List<NhPhieuNhapKhoCt1> chiTiet1s = new ArrayList<>();

    private ReportTemplateRequest reportTemplateRequest;
}
