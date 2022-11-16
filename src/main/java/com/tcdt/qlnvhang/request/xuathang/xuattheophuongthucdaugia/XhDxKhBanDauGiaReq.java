package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaPhanLo;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhDxKhBanDauGiaReq {
    private Long id;
    private String maDvi;
    private String tenDvi;
    private String diaChi;
    private String loaiHinhNx;
    private String kieuNx;
    private Integer namKh;
    private String soDxuat;
    private String trichYeu;
    private String soQdCtieu;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String tchuanCluong;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienDen;
    private String loaiHdong;
    private Integer tgianKyHdong;
    private Integer tgianTtoan;
    private Integer tgianGnhan;
    private String thongBaoKh;
    private Integer khoanTienDatTruoc;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienKdiem;
    private BigDecimal tongTienDatTruoc;
    private String ghiChu;
    private String maThop;
    private String soQdPd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private Integer soDviTsan;
    private Integer slHdDaKy;
    private String ldoTuChoi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();


    private List<XhDxKhBanDauGiaPhanLoReq> dsPhanLoReq = new ArrayList<>();


}
