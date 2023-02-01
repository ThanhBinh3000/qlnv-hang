package com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdGiaoNvuXuatReq {
    private Long id;
    private Integer nam;

    private String soQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;
    private String soHd;
    private String tenTtcn;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String donViTinh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGnhan;
    private String trichYeu;
    private BigDecimal soLuong;

    private String bbTinhKho;

    private String bbHaoDoi;
    
    private List<XhQdGiaoNvuXuatCtReq> cts = new ArrayList<>();

//    private List<Long> hopDongIds = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private FileDinhKemReq fileDinhKem;
}
