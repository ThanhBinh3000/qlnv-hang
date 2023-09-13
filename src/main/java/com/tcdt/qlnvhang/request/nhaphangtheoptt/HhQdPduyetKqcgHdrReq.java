package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttSLDDReq;
import com.tcdt.qlnvhang.request.HhQdPheduyetKqMttSLDDReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.HhQdPheduyetKqMttSLDD;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;


import javax.persistence.Column;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhQdPduyetKqcgHdrReq extends BaseRequest {
    private Long id;

    private Long idPdKhDtl;

    private Long idPdKhHdr;

    private Integer namKh;

    private String soQdKq;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayKy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayHluc;

    private String soQd;

    private String trichYeu;

    private String maDvi;

    private String diaDiemChaoGia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayMua;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String ghiChu;

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    private FileDinhKemReq fileDinhKem;

    private List<HhQdPheduyetKqMttSLDD> danhSachCtiet = new ArrayList<>();
}
