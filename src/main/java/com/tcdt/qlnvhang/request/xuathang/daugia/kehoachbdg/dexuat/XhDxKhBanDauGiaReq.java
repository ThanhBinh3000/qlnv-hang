package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhDxKhBanDauGiaReq extends BaseRequest {
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienDen;

    private Integer tgianTtoan;

    private Integer tgianGnhan;

    private String pthucTtoan;

    private String pthucGnhan;

    private String tgianTtoanGhiChu;

    private String tgianGnhanGhiChu;

    private String thongBao;

    private BigDecimal khoanTienDatTruoc;

    private BigDecimal tongSoLuong;

    private BigDecimal tongTienGiaKhoiDiemDx;

    private BigDecimal tongKhoanTienDatTruocDx;

    private String ghiChu;

    private Integer slDviTsan;

    private String trangThaiTh;

    private Long idSoQdPd;

    private String soQdPd;

    private Long idThop;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;

    private String donViTinh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;

    private List<String> trangThaiList = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<XhDxKhBanDauGiaDtl> children = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoDen;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDuyetTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDuyetDen;

}
