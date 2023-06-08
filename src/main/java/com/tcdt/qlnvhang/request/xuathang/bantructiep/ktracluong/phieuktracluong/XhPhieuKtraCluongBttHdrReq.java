package com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhPhieuKtraCluongBttHdrReq extends BaseRequest {

    private Long id;

    private Integer namKh;

    private String maDvi;

    private String maQhns;

    private Long idBienBan;

    private String soBienBan;

    private Long idQdNv;

    private String soQdNv;

    private LocalDate ngayQd;

    private String soPhieu;

    private Long idNgKnghiem;

    private Long idTruongPhong;

    private Long idKtv;

    private Long idDdiemXh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private BigDecimal soLuong;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String hthucBquan;

    private LocalDate ngayLayMau;

    private LocalDate ngayKnghiem;

    private String ketQua;

    private String ketLuan;

    private String soBbXuatDoc;

    private LocalDate ngayXuatDocKho;

    private String maChiCuc;

    private LocalDate ngayKnghiemTu;

    private LocalDate ngayKnghiemDen;

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<XhPhieuKtraCluongBttDtlReq> children = new ArrayList<>();
}
