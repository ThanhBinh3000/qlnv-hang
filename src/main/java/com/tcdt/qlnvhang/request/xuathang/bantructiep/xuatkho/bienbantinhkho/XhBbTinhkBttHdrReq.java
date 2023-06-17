package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhBbTinhkBttHdrReq extends BaseRequest {

    private Long id;

    private Integer namKh;

    private String maDvi;

    private String maQhns;

    private String soBbTinhKho;

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

    private LocalDate ngayBdauXuat;

    private LocalDate ngayKthucXuat;

    private BigDecimal tongSlNhap;

    private BigDecimal tongSlXuat;

    private BigDecimal slConLai;

    private BigDecimal slThucTe;

    private BigDecimal slThua;

    private BigDecimal slThieu;

    private String nguyenNhan;

    private String kienNghi;

    private String ghiChu;

    private Long idThuKho;

    private Long idKtv;

    private Long idKeToan;

    private String phanLoai;

    private String pthucBanTrucTiep;

    private Long idBangKeBanLe;

    private String soBangKeBanLe;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private LocalDate ngayTaoBangKe;

    private LocalDate ngayBdauXuatTu;

    private LocalDate ngayBdauXuatDen;

    private LocalDate ngayKthucXuatTu;

    private LocalDate ngayKthucXuatDen;

    private LocalDate ngayQdNvTu;

    private LocalDate ngayQdNvDen;

    @Transient
    private List<XhBbTinhkBttDtlReq> children = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
}
