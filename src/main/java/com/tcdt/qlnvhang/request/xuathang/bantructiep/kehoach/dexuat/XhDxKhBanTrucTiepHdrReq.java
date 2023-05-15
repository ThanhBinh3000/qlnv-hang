package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDxKhBanTrucTiepHdrReq extends BaseRequest {

    private Long id;

    private String maDvi;

    private String loaiHinhNx;

    private String kieuNx;

    private String diaChi;

    private Integer namKh;

    private String soDxuat;

    private String trichYeu;

    private Long idSoQdCtieu;

    private String soQdCtieu;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String tchuanCluong;

    private LocalDate tgianDkienTu;

    private LocalDate tgianDkienDen;

    private Integer tgianTtoan;

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;

    private BigDecimal tongSoLuong;

    private String ghiChu;

    private String trangThaiTh;

    private String donViTinh;

    private LocalDate ngayTao;

    private LocalDate ngayTaoTu;

    private LocalDate ngayTaoDen;

    private LocalDate ngayDuyetTu;

    private LocalDate ngayDuyetDen;

    private LocalDate ngayKyQdTu;

    private LocalDate ngayKyQdDen;

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<XhDxKhBanTrucTiepDtl> children = new ArrayList<>();
}
