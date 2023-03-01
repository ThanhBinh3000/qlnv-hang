package com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat;

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
public class XhQdNvXhBttHdrReq extends BaseRequest {
    private Long id;

    private Integer namKh;

    private String soQd;

    private String maDvi;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;

    private Long idHd;

    private String soHd;

    private Long idQdPdKh;

    private String soQdPdKh;

    private String maDviTsan;

    private String tenTtcn;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private BigDecimal soLuong;

    private String donViTinh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGnhan;

    private String trichYeu;

    private String phanLoai;

    private List<XhQdNvXhBttDtlReq> children = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    private FileDinhKemReq fileDinhKem;



    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyDen;
}

