package com.tcdt.qlnvhang.request;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhChiTietTTinChaoGiaReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhQdPheduyetKhMttHdrReq {
    @ApiModelProperty(notes = "bắt buộc set phải đối với update")
    private Long id;
    private Integer namKh;
    private String soQdPduyet;
    private Long idDxuat;
    private String soDxuat;
    private Long idThop;
    private String maThop;
    private String maDvi;
    private String trichYeu;
    private String trangThai;
    private String trangThaiTkhai;

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();

    private List<HhQdPheduyetKhMttDxReq> hhQdPheduyetKhMttDxList = new ArrayList<>();

    private List<HhChiTietTTinChaoGiaReq> hhChiTietTTinChaoGiaReqList = new ArrayList<>();
}
