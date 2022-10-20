package com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCt1;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhPhieuNhapKhoReq extends BaseRequest {


    private List<Long> phieuKtClIds = new ArrayList<>();
    private String soPhieu;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianGiaoNhan;

    private Long qdgnvnxId;

    private List<NhPhieuNhapKhoCtReq> hangHoaList = new ArrayList<>();

    private List<FileDinhKemReq> chungTus;

    private Long id;

    private String soPhieuKtraCl;

    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    private String soPhieuNhapKho;

    private String soHd;

    private LocalDate ngayHd;

    private LocalDate ngayNhapKho;

    private String nguoiGiaoHang;

    private LocalDate ngayTaoPhieu;

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

    private String diaChi;

    private String keToanTruong;

    private String ghiChu;

    private Long idDdiemGiaoNvNh;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<NhPhieuNhapKhoCt1> chiTiet1s = new ArrayList<>();
}
