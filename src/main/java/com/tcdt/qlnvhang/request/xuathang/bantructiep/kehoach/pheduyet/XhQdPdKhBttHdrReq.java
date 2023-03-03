package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdPdKhBttHdrReq extends BaseRequest {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Integer namKh;

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

    private String maDviTsan;

    private String soHopDong;

    private String soQdCc;

    private String trangThaiChaoGia;

    @Transient
    private List<XhQdPdKhBttDtlReq> children;

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private FileDinhKemReq fileDinhKem;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdDen;

    private String maCuc;

}
