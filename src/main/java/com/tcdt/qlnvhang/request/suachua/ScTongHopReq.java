package com.tcdt.qlnvhang.request.suachua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttSLDDReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopDtl;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ScTongHopReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maDanhSach;
    private String tenDanhSach;
    private LocalDate thoiGianThTu;
    private LocalDate thoiGianThDen;
    private String trangThai;

}
