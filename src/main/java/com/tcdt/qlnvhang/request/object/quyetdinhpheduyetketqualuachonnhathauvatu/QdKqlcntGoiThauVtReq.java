package com.tcdt.qlnvhang.request.object.quyetdinhpheduyetketqualuachonnhathauvatu;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class QdKqlcntGoiThauVtReq {
    private Long id;

    @NotNull(message = "Không được để trống")
    @Size(max = 500, message = "Số quyết định không được vượt quá 500 ký tự")
    private String tenGoiThau;

    @NotNull(message = "Không được để trống")
    private BigDecimal soLuong;

    @NotNull(message = "Không được để trống")
    private BigDecimal giaGoiThau;

    @NotNull(message = "Không được để trống")
    private String tenDonViTrungThau;

    @NotNull(message = "Không được để trống")
    private Long loaiHopDongId;

    @NotNull(message = "Không được để trống")
    private String thoiGianThucHien;

    @NotNull(message = "Không được để trống")
    private BigDecimal donGiaTruocThue;

    @NotNull(message = "Không được để trống")
    private BigDecimal thueVat;

    @NotNull(message = "Không được để trống")
    private BigDecimal donGiaSauThue;

    @NotNull(message = "Không được để trống")
    private BigDecimal donGiaHopDongTruocThue;

    @NotNull(message = "Không được để trống")
    private BigDecimal donGiaHopDongSauThue;

    @NotNull(message = "Không được để trống")
    private String trangThai;

    private List<QdKqlcntGtDdnVtReq> ddnReqs = new ArrayList<>();
}
