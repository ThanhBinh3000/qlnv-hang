package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class KhCnCongTrinhNghienCuuReq {
    private Long id;
    private String nam;
    private String maDeTai;
    private String tenDeTai;
    private String capDeTai;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyDen;
    private String chuNhiem;
    private String chucVu;
    private String email;
    private String sdt;
    private String dviChuTri;
    private String tenDviChuTri;
    private String tenDviThucHien;
    private String dviPhoiHop;
    private String dviThucHien;
    private String nguonVon;
    private String soQdPd;
    @Lob
    private String suCanThiet;
    @Lob
    private String mucTieu;
    @Lob
    private String phamVi;
    @Lob
    private String noiDung;
    @Lob
    private String phuongPhap;
    private BigDecimal tongChiPhi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGduyet;
    private Long nguoiGduyetId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private Long nguoiPduyetId;
    private String trangThai;
    private String maDvi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNghiemThu;
    private String diaDiem;
    @Lob
    private String danhGia;
    private Integer tongDiem;
    private String xepLoai;
    private String ldoTuChoi;

    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

    private List<FileDinhKemReq> fileTienDoTh = new ArrayList<>();

    private List<FileDinhKemReq> fileNghiemThuTl = new ArrayList<>();

    private List<KhCnTienDoThucHienReq> tienDoThucHien = new ArrayList<>();

    private List<KhCnNghiemThuThanhLyReq> children = new ArrayList<>();

}
