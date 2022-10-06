package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhPhuLucHopDongMttReq {
    private Long id;
    private Long idHdHdr;
    private String soHdong;
    private String tenHdong;
    private Integer phuLucSo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHlucPluc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;
    private String veViec;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHlucGocTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHlucGocDen;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHlucDcTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHlucDcDen;
    private Integer tgianThucHienGoc;
    private Integer tgianThucHienDc;
    private String noiDungDc;
    private String ghiChu;
    private String trangThai;

    private List<HhDcDiaDiemGiaoNhanHangReq> dcDiaDiemGiaoNhanHangList=new ArrayList<>();
    private List<FileDinhKemReq> FileDinhKems =new ArrayList<>();
}
