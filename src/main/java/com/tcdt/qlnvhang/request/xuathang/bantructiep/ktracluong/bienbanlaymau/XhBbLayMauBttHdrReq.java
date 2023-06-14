package com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.bienbanlaymau;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhBbLayMauBttHdrReq extends BaseRequest {

    private Long id;

    private Integer namKh;

    private String maDvi;

    private String maQhns;

    private Long idQdNv;

    private String soQdNv;

    private LocalDate ngayQd;

    private Long idHd;

    private String soHd;

    private String loaiBienBan;

    private LocalDate ngayKyHd;

    private Long idKtv;

    private String soBienBan;

    private LocalDate ngayLayMau;

    private String dviKnghiem;

    private String ddiemLayMau;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private Long idDdiemXh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private BigDecimal soLuongLayMau;

    private String ppLayMau;

    private String chiTieuKiemTra;

    private Integer ketQuaNiemPhong;

    private String soBbTinhKho;

    private LocalDate ngayXuatDocKho;

    private String soBbHaoDoi;

    private String pthucBanTrucTiep;

    @Transient
    private List<XhBbLayMauBttDtlReq> children = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileNiemPhong = new ArrayList<>();

    private LocalDate ngayLayMauTu;

    private LocalDate ngayLayMauDen;
}
