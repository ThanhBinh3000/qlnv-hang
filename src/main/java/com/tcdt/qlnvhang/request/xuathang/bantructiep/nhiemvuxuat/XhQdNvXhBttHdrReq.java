package com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdNvXhBttHdrReq extends BaseRequest {

    private Long id;

    private String maDvi;

    private Integer namKh;

    private String soQdNv;

    private LocalDate ngayQdNv;

    private Long idHd;

    private String soHd;

    private Long idQdPd;

    private Long idQdPdDtl;

    private String soQdPd;

    private String maDviTsan;

    private String tenTccn;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String loaiHinhNx;

    private String kieuNx;

    private BigDecimal soLuongBanTrucTiep;

    private String donViTinh;

    private LocalDate tgianGnhan;

    private String trichYeu;

    private String trangThaiXh;

    private String pthucBanTrucTiep;

    private LocalDate ngayKyHd;

    private String phanLoai;

    private List<XhQdNvXhBttDtlReq> children = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    private LocalDate ngayTaoTu;

    private LocalDate ngayTaoDen;

    private String maChiCuc;

    private String soBienBan;

    private LocalDate ngayLayMauTu;

    private LocalDate ngayLayMauDen;
}

