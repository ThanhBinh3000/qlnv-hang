package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.phieuxuatkho;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhPhieuXkhoBttReq extends BaseRequest {

    private Long id;

    private Integer namKh;

    private String maDvi;

    private String maQhns;

    private String soPhieuXuat;

    private LocalDate ngayXuatKho;

    private BigDecimal no;

    private BigDecimal co;

    private Long idQdNv;

    private String soQdNv;

    private LocalDate ngayQdNv;

    private Long idHd;

    private String soHd;

    private LocalDate ngayKyHd;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private Long idPhieu;

    private String soPhieu;

    private LocalDate ngayKnghiem;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private Long idNguoiLapPhieu;

    private Long idKtv;

    private String keToanTruong;

    private String nguoiGiao;

    private String cmtNguoiGiao;

    private String ctyNguoiGiao;

    private String diaChiNguoiGiao;

    private LocalDate tgianGiaoNhan;

    private String soBangKe;

    private String  maSo;

    private String donViTinh;

    private BigDecimal soLuongChungTu;

    private BigDecimal soLuongThucXuat;

    private BigDecimal donGia;

    private String ghiChu;

    @Transient
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
}
