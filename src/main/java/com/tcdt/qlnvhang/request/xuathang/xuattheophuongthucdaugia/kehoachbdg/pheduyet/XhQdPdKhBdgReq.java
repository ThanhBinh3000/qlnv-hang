package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.kehoachbdg.pheduyet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdPdKhBdgReq extends BaseRequest {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    @ApiModelProperty(example = "2022")
    private Integer nam;

    private String maDvi;

    @Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "20-QD/TCDT")
    private String soQdPd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private Long idThHdr;

    private String soTrHdr;

    private Long idTrHdr;

    private String trichYeu;

    @Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "00")
    private String loaiVthh;

    private String cloaiVthh;

    private String  moTaHangHoa;

    private String tchuanCluong;

    private Integer lastest ;

    private String phanLoai;

    private List<XhQdPdKhBdgDtlReq> children;

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdDen;

}

