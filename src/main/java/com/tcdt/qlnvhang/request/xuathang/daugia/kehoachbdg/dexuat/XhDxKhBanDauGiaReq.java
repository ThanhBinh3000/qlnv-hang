package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String pthucGnhan;
    private String tgianGnhanGhiChu;
    private String thongBao;
    private BigDecimal khoanTienDatTruoc;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienKdiem;
    private BigDecimal tongTienKdienDonGia;
    private BigDecimal tongTienDatTruoc;
    private BigDecimal tongTienDatTruocDonGia;
    private String ghiChu;
    private Integer slDviTsan;
    private String trangThaiTh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoDen;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDuyetTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDuyetDen;

    private List<String> trangThaiList = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    
    private List<XhDxKhBanDauGiaDtlReq> children = new ArrayList<>();

}
