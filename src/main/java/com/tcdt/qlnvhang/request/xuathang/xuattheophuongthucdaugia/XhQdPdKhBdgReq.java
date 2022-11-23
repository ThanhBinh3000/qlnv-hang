package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdPdKhBdgReq {
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String tenDvi;
    private String soQdPd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;
    private Long idThop;
    private String maThop;
    private Long idDxuat;
    private String soDxuat;
    private String trichYeu;
    private String loaiVthh;
    private String cloaiVthh;
    private String trangThai;
    private Integer soDviTsan;
    private Integer slHdDaKy;

    List<XhQdPdKhBdgDtlReq> qdPdKhBdgDtlReq = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();
}

