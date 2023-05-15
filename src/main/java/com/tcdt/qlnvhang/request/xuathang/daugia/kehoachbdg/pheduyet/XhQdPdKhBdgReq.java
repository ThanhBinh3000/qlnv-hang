package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBdgReq extends BaseRequest {

    private Long id;

    private Integer nam;

    private String maDvi;

    private String soQdPd;

    private LocalDate ngayKyQd;

    private LocalDate ngayHluc;

    private Long idThHdr;

    private String soTrHdr;

    private Long idTrHdr;

    private String trichYeu;

    private String loaiVthh;

    private String cloaiVthh;

    private String  moTaHangHoa;

    private String tchuanCluong;

    private Integer lastest ;

    private String phanLoai;

    private String soQdCc;

    private Integer slDviTsan;

    private String loaiHinhNx;

    private String kieuNx;

    private List<XhQdPdKhBdgDtlReq> children;

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

    private LocalDate ngayKyQdTu;

    private LocalDate ngayKyQdDen;
}

