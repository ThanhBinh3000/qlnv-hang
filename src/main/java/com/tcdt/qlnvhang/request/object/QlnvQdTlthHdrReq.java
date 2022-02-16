package com.tcdt.qlnvhang.request.object;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class QlnvQdTlthHdrReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    @Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
    @ApiModelProperty(example = "SQD123")
    String soQdinh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayQdinh;

    @NotNull(message = "Không được để trống")
    @Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
    @ApiModelProperty(example = "MHH001")
    String maHhoa;

    @NotNull(message = "Không được để trống")
    @Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
    @ApiModelProperty(example = Contains.MOI_TAO)
    String trangThai;

    @Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
    String ldoTuchoi;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date tuNgayThien;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date denNgayThien;

    @NotNull(message = "Không được để trống")
    @Size(max = 50, message = "Loại hình xuất không được vượt quá 2 ký tự")
    @ApiModelProperty(example = "00")
    String lhinhXuat;

    private List<QlnvQdTlthDtlReq> detailListReq;
}
