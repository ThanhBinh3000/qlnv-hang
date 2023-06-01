package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.util.List;

@Data
public class HhThopKhNhapKhacReq {
    private Long id;
    private Integer namKhoach;
    private String loaiHinhNx;
    private String loaiVthh;
    private String maTh;
    private String noiDungTh;
    private List<FileDinhKemReq> fileDinhKems;
    private List<HhDxuatKhNhapKhacHdrReq> details;
}
