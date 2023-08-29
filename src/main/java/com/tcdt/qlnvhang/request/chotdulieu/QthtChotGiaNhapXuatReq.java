package com.tcdt.qlnvhang.request.chotdulieu;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class QthtChotGiaNhapXuatReq extends BaseRequest {

    private Long id;

    private LocalDate ngayChot;

    private LocalDate ngayHluc;

    private LocalDate ngayHlucQdDC;

    private List<String> listMaDvi = new ArrayList<>();

    private String type;
}
