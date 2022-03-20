package com.tcdt.qlnvhang.request.object.quyetdinhpheduyetketqualuachonnhathauvatu;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class QdPheDuyetKqlcntVtReq {

    private Long id;

    @NotNull(message = "Không được để trống")
    @Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
    private String soQuyetDinh;

    @NotNull(message = "Không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayQuyetDinh;

    @NotNull(message = "Không được để trống")
    private Integer namKeHoach;

    private String veViec;

    @NotNull(message = "Không được để trống")
    private Long vatTuId;

    @NotNull(message = "Không được để trống")
    private Long khLcntVtId;

    @NotNull(message = "Không được để trống")
    private Long thongTinDauThauId;

}
