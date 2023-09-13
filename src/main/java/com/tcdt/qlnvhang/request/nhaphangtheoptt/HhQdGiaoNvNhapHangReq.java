package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhQdGiaoNvNhapHangReq extends BaseRequest {
    private Long id;
    private Integer namNhap;
    private String soQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyHd;
    private String maDvi;
    private String tenDvi;
    private String idHd;
    private String soHd;
    private String tenHd;
    private String loaiQd;
    private Long idQdPdKh;
    private String soQdPdKh;
    private Long idQdPdKq;
    private String soQdPdKq;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String donViTinh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianNkho;
    private String trichYeu;
    private String trangThai;
    private BigDecimal soLuong;

    private List<HhQdGiaoNvNhangDtlReq> hhQdGiaoNvNhangDtlList= new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
    private FileDinhKemReq fileDinhKem;
}
