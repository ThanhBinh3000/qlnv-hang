package com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdDchinhKhBttHdrReq extends BaseRequest {

    private Long id;

    private Integer namKh;

    private String maDvi;

    private String soQdDc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyDc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private String loaiHinhNx;

    private String kieuNx;

    private String trichYeu;

    private String soQdGoc;

    private Long idQdGoc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdGoc;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyDcTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyDcDen;

    @Transient
    List<XhQdDchinhKhBttDtlReq> children = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
}
