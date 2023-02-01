package com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdGiaoNvuXuatCtReq {
    private Long id;
    private Long idQdHdr;
    private String maDvi;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private String trangThai;

    private List<XhQdGiaoNvXhDdiemReq> xhQdGiaoNvXhDdiemList = new ArrayList<>();
}
