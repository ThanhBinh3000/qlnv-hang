package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.HhDthauNthauDuthauReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HhCgiaReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idChaoGia;

    private String loaiVthh;

    private String trangThaiTkhai;

    private String ptMuaTrucTiep;

    private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();

    private List<HhChiTietTTinChaoGiaReq> hhChiTietTTinChaoGiaReqs = new ArrayList<>();
}
