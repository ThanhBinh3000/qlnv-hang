package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttSLDDReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhCgiaReq extends BaseRequest {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idQdDtl;

    private String soQd;

    private String pthucMuaTrucTiep;

    private String diaDiemChaoGia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayMua;

    private String ghiChuChaoGia;

    private String trangThai;

    private List<FileDinhKemReq> fileDinhKemUyQuyen = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKemMuaLe = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

    private List<HhChiTietTTinChaoGiaReq> children = new ArrayList<>();
    private List<HhQdPheduyetKhMttSLDDReq> danhSachCtiet = new ArrayList<>();
}
