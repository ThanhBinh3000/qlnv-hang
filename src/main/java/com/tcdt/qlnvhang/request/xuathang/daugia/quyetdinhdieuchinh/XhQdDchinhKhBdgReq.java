package com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBdgReq extends BaseRequest {

    private Long id;

    private Integer nam;

    private String maDvi;

    private String soQdDc;

    private LocalDate ngayKyDc;

    private LocalDate ngayHlucDc;

    private String soQdGoc;

    private Long idQdGoc;

    private LocalDate ngayKyQdGoc;

    private String trichYeu;

    private String soQdCc;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String loaiHinhNx;

    private String kieuNx;

    private String tchuanCluong;

    private Integer slDviTsan;

    @Transient
    private List<XhQdDchinhKhBdgDtlReq> children = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

    private LocalDate ngayKyDcTu;

    private LocalDate ngayKyDcDen;
}

