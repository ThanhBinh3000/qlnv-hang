package com.tcdt.qlnvhang.response.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGoiThauVt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class QdPheDuyetKqlcntVtRes {

    private Long id;
    private String soQuyetDinh;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayQuyetDinh;
    private Integer namKeHoach;
    private String veViec;
    private Long vatTuId;
    private String maVatTu;
    private String tenVatTu;
    private Long khLcntVtId;
    private Long thongTinDauThauId;
    private String trangThai;
    private String tenTrangThai;

    private List<QdKqlcntGoiThauVtRes> goiThaus = new ArrayList<>();
}
