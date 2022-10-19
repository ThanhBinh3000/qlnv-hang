package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

@Data
public class HhDcQdPduyetKhmttHdrReq {
    private Long id;
    private String soQdDc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyDc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;
    private String trichYeu;
    private Long idQdGoc;
    private String soQdGoc;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String trangThai;
    private Integer namKh;
    private String maDvi;

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();


    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();

    private List<HhDcQdPduyetKhmttDxReq> hhDcQdPduyetKhmttDxList=new ArrayList<>();
}
