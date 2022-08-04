package com.tcdt.qlnvhang.response.xuathang.quyetdinhgiaonhiemvuxuat;

import com.tcdt.qlnvhang.response.CommonResponse;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class XhQdGiaoNvuXuatRes extends CommonResponse {
    private Long id;

    private String soQuyetDinh;

    private LocalDate ngayQuyetDinh;

    private Integer namXuat;

    private String trichYeu;

    private String loaiVthh;
    
    private List<XhQdGiaoNvuXuatCtRes> cts = new ArrayList<>();

    private List<Long> hopDongIds = new ArrayList<>();

    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
