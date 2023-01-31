package com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdDchinhKhBdgReq extends BaseRequest {
    private Long id;

    private Integer nam;

    private String maDvi;

    private String soQdDc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyDc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHlucDc;

    private String soQdGoc;

    private Long idQdGoc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdGoc;

    private String trichYeu;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String soQdCc;

    private String tchuanCluong;

    private List<XhQdPdKhBdgDtlReq> children = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdDen;

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();
}

