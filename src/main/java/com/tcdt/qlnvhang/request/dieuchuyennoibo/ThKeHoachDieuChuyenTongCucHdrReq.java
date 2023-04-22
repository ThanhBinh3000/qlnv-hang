package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.Date;

@Data
public class ThKeHoachDieuChuyenTongCucHdrReq {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngaytao;

    private Long nguoiTaoId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngaySua;

    private Long nguoiSuaId;

    private String maTongHop;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTongHop;

    private String noiDung;

    private Integer namKeHoach;

    private String loaiDieuChuyen;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thTuNgay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thDenNgay;

    private String loaiHangHoa;

    private String tenLoaiHangHoa;

    private String chungLoaiHangHoa;

    private String trangThai;

    private String maDVi;

    private String tenDVi;
}
