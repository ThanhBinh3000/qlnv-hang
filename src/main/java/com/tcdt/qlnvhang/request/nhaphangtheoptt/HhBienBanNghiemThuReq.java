package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhBienBanNghiemThuReq {
    private Long id;
    private Long idQdNh;
    private String soQdNh;
    private String soBb;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNghiemThu;
    private String thuTruong;
    private String keToan;
    private String kyThuatVien;
    private String thuKho;
    private String lhKho;
    private Double slThucNhap;
    private Double tichLuong;
    private String pthucBquan;
    private String hthucBquan;
    private Double dinhMuc;
    private String ketLuan;
    private String trangThai;
    private String ldoTuchoi;
    private String capDvi;
    private String maDvi;
    private Integer namKh;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private Long hopDongId;
    private String maQhns;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;

    private List<HhBbanNghiemThuDtlReq> bbanNghiemThuDtlList =new ArrayList<>();

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();
}
