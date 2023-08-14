package com.tcdt.qlnvhang.request.suachua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBaoCaoDtl;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ScBaoCaoReq extends BaseRequest {

    private Long id;
    private Integer nam;
    private String maDviNhan;
    private String soBaoCao;
    private LocalDate ngayBaoCao;
    private String tenBaoCao;
    private String soQdXh;
    private Long idQdXh;
    private LocalDate ngayQdXh;
    private String soQdTc;
    private LocalDate ngayQdTc;
    private String noiDung;
    private String trangThai;

    private List<ScBaoCaoDtl> children = new ArrayList<>();

    //Param
    private String maDviSr;
    private LocalDate ngayTu;
    private LocalDate ngayDen;

}
