package com.tcdt.qlnvhang.request.chotdulieu;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.chotdulieu.QthtKetChuyenDtl;
import com.tcdt.qlnvhang.table.chotdulieu.QthtKetChuyenHdr;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class QthtKetChuyenReq extends BaseRequest {

    private Long id;
    private Integer nam;

    private Date ngayTu;

    private Date ngayDen;

    private String maDvi;

    private String tenVietTat;

    private String tenChiCuc;

    private String tenNguoiTao;

    private List<QthtKetChuyenDtl> children = new ArrayList<>();

    private String maDviSr;
}
