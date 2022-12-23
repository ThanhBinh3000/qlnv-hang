package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;

import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

@Data
public class HhHdongBkePmuahangReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Integer  namHd;
    private String soQdKqMtt;
    private Long idQdKqMtt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayQdKqMtt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianNkho;

    private String soQdPdKhMtt;

    private String soHd;
    private String tenHd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayKy;
    private String ghiChuNgayKy;
    private String loaiHdong;
    private String ghiChuLoaiHdong;
    private Integer soNgayThien;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date tgianGnhanTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date tgianGnhanDen;
    private Long idChaoGia;

    private String ghiChuTgianGnhan;
    private String noiDung;
    private  String dieuKien;

    private String maDvi;


    private String diaChi;

    private String mst;

    private String tenNguoiDdien;

    private String chucVu;

    private String sdt;

    private String fax;

    private String stk;

    private String moLai;

    private String thongTinGianUyQuyen;

    private String tenCongTy;

    private String diaChiCongTy;

    private String mstCongTy;

    private String tenNguoiDdienCongTy;

    private String chucVuCongTy;

    private String sdtCongTy;

    private String faxCongTy;

    private String stkCongTy;

    private String moLaiCongTy;

    private String loaiVthh;


    private String cloaiVthh;


    private String moTaHangHoa;



    private Double soLuong;
    private Double donGia;

    private Double thanhTien;

    private String ghiChu;

    private BigDecimal soLuongQdKhMtt;

    private String trangThai;


    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;
    private Date ngayPduyet;
    private String nguoiPduyet;
    private Integer namKh;

    private List<HhThongTinDviDtuCcapReq> detail;

    private List<FileDinhKemReq> fileDinhKems;


}
