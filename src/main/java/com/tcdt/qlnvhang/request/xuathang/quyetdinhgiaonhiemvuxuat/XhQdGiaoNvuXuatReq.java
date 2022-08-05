package com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhQdGiaoNvuXuatReq {
    private Long id;

    private String soQuyetDinh;

    private LocalDate ngayQuyetDinh;

    private Integer namXuat;

    private String trichYeu;

    private String loaiVthh;
    
    private List<XhQdGiaoNvuXuatCtReq> cts = new ArrayList<>();

    private List<Long> hopDongIds = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
}
