package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScQuyetDinhScReq extends BaseRequest {
    private Long id;
    private String soQd;
    private String trichYeu;
    private LocalDate ngayKy;
    private String soTtr;
    private Long idTtr;
    private LocalDate ngayDuyetLdtc;
    private LocalDate thoiHanXuat;
    private LocalDate thoiHanNhap;
    private String trangThai;

    private List<FileDinhKemReq> fileDinhKemReq =new ArrayList<>();
    private List<FileDinhKemReq> fileCanCuReq = new ArrayList<>();

    //Param search

    private LocalDate ngayTu;
    private LocalDate ngayDen;

}
