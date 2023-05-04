package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class XhQdPdKhBdgDtlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idHdr;
    private Long idDxHdr;

    @NotNull(message = "Không được để trống")
    @Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "HNO")
    String maDvi;

    @NotNull(message = "Không được để trống")
    @Size(max = 20, message = "Số đề xuất không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "Tên dự án")
    String soDxuat;

    @NotNull(message = "Không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayTao;

    @NotNull(message = "Không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayPduyet;

    private String trichYeu;

    private BigDecimal tongSoLuong;

    private Integer slDviTsan;

    private String moTaHangHoa;

    private String diaChi;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date tgianDkienTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date tgianDkienDen;

    private Integer tgianTtoan;

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;

    private BigDecimal khoanTienDatTruoc;

    private String donViTinh;

    private List<XhQdPdKhBdgPlReq> children;
}
