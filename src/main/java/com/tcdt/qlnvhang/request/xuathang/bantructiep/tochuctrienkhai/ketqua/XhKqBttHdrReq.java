package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhKqBttHdrReq extends BaseRequest  {

    private Long id;

    private Long idPdKhDtl;

    private Long idPdKhHdr;

    private Integer namKh;

    private String soQdKq;

    private LocalDate ngayKy;

    private LocalDate ngayHluc;

    private String soQdPd;

    private String trichYeu;

    private String maDvi;

    private String diaDiemChaoGia;

    private LocalDate ngayMkho;

    private LocalDate ngayKthuc;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String ghiChu;

    private String loaiHinhNx;

    private String kieuNx;

    private String soDxuat;

    @Transient
    private List<FileDinhKemReq> fileCanCu = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileQdDaKy = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileQd = new ArrayList<>();

    @Transient
    private List<XhKqBttDtlReq> children = new ArrayList<>();

    private String maChiCuc;

    private LocalDate  ngayCgiaTu;

    private LocalDate  ngayCgiaDen;

    private String trangThaiHd;

    private String soHd;

    private String tenHd;

    private String tenDviMua;

    private LocalDate  ngayPduyetTu;

    private LocalDate ngayPduyetDen;
}
