package com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBttHdrReq extends BaseRequest {

    private Long id;

    private Integer namKh;

    private String maDvi;

    private String soQdDc;

    private LocalDate ngayKyDc;

    private LocalDate ngayHluc;

    private String loaiHinhNx;

    private String kieuNx;

    private String trichYeu;

    private String soQdGoc;

    private Long idQdGoc;

    private LocalDate ngayKyQdGoc;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private LocalDate ngayKyDcTu;

    private LocalDate ngayKyDcDen;

    @Transient
    List<XhQdDchinhKhBttDtlReq> children = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
}
