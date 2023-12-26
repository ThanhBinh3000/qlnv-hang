package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbQuyetDinhDcTcHdrPreviewReq implements Serializable {
    private Long id;
    private Integer nam;
    private List<DcnbQuyetDinhDcTcDtlPreviewReq> listDtl =  new ArrayList<>();

}
