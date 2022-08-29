package com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import com.tcdt.qlnvhang.response.CommonResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBangKeCanHangLtRes extends CommonResponse {
    private Long id;
    private String soBangKe;
    private Long qdgnvnxId;
    private String soQuyetDinhNhap;
    private Long qlPhieuNhapKhoLtId;
    private String soPhieuNhapKho;
    private String donViTinh;
    private String tenNguoiGiaoHang;
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;
    private String soHd;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNganLo;
    private String tenNganLo;
    private String diaChiNguoiGiao;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayNhap;
    private String thuKho;
    private String tenVatTu;
    private String maVatTu;
    private String maVatTuCha;
    private String tenVatTuCha;
    private String diaDiem;

    private BigDecimal tongTrongLuongBaoBi;
    private BigDecimal tongTrongLuongCaBi;
    private String tongTrongLuongHangTruBiBangChu;

    private String maDvi;
    private String tenDvi;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private List<QlBangKeChCtLtRes> chiTiets = new ArrayList<>();
}
