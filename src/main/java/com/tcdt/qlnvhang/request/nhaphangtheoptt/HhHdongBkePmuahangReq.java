package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhHdongBkePmuahangReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Integer  namHd;
    private Long idQdCgia;
    private String soQdCgia;
    private String dviCcap;
    private String idQdMtt;
    private String soQdMtt;
    private String soHdong;
    private String tenHdong;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;
    private String ghiChuNgayHd;
    private String loaiHdong;
    private String ghiChuLoaiHd;
    private Integer tgianThucHien;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianNhanHangTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianNhanHangDen;
    private String ghiChuNhanHang;
    private String noiDung;
    private String dieuKien;
    private String maDvi;
    private String trangThai;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String donViTinh;
    private BigDecimal soLuong;
    private BigDecimal giaCoThue;
    private BigDecimal thanhTien;
    private String ghiChu;
    private BigDecimal soLuongTheoQdpdKh;

    private List<FileDinhKemReq> FileDinhKems =new ArrayList<>();

    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();

    private List<HhThongTinDviDtuCcapReq> thongTinChuDauTu;

    private List<HhThongTinDviDtuCcapReq> thongTinDviCungCap;

    private List<HhDiaDiemGiaoNhanHangReq> diaDiemGiaoNhanHangList=new ArrayList<>();

}
