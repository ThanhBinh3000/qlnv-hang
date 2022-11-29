package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class KhCnCongTrinhNghienCuuReq {
    private Long id;

    private String maDeTai;
    private String tenDeTai;
    private String capDetai;
    private Integer tuNam;
    private Integer denNam;
    private String chuNhiem;
    private String chucVu;
    private String email;
    private String sdt;
    private String suCanThiet;
    private String mucTieu;
    private String phamVi;
    private String noiDung;
    private String phuongPhap;
    private BigDecimal tongChiPhi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGduyet;
    private String nguoiGduyetId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private String nguoiPduyetId;
    private String trangThai;
    private String maDvi;



    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    private List<KhCnTienDoThucHienReq> tienDoThucHien=new ArrayList<>();

    private KhCnNghiemThuThanhLyReq nghiemThu;
}
