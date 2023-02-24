package com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong;

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
public class XhHopDongBttHdrReq extends BaseRequest {
    private Long id;

    private Long idQdKq;

    private Integer namHd;

    private String soQdKq;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdKq;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayMkho;

    private String soQdPd;

    private String maDviTsan;

    private String soHd;

    private String tenHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private String ghiChuNgayHluc;

    private String loaiHdong;

    private String ghiChuLoaiHdong;

    private Integer tgianThienHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGnhanTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGnhanDen;

    private String ghiChuTgianGnhan;

    private String noiDungHdong;

    private String dkienHanTtoan;

    private String maDvi;

    private String diaChi;

    private String mst;

    private String tenNguoiDdien;

    private String chucVu;

    private String sdt;

    private String fax;

    private String stk;

    private String moLai;

    private String ttinGiayUyQuyen;

    private Long idDviMua;

    private String tenDviMua;

    private String diaChiDviMua;

    private String mstDviMua;

    private String tenNguoiDdienDviMua;

    private String chucVuDviMua;

    private String sdtDviMua;

    private String faxDviMua;

    private String stkDviMua;

    private String moLaiDviMua;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String dviTinh;

    private BigDecimal soLuong;

    private BigDecimal donGiaGomThue;

    private BigDecimal donGia;

    private String ghiChu;

    private BigDecimal soLuongQdChuaKy;

    private BigDecimal tongTien;

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();
    @Transient
    private List<XhHopDongBttDtlReq> children = new ArrayList<>();

}
