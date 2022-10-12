package com.tcdt.qlnvhang.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhChiTietTTinChaoGiaReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhQdPheduyetKhMttHdrReq {
    @ApiModelProperty(notes = "bắt buộc set phải đối với update")
    private Long id;
    private Integer namKh;
    private String soQdPduyet;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date NgayHluc;
    private Long idDxuat;
    private String soDxuat;
    private Long idThop;
    private String maThop;
    private String maDvi;
    private String trichYeu;
    private String trangThai;
    private String trangThaiTkhai;
    private String pthucMuatt;
    private String diaDiemCgia;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();

    private List<HhQdPheduyetKhMttDxReq> hhQdPheduyetKhMttDxList = new ArrayList<>();

    private List<HhChiTietTTinChaoGiaReq> hhChiTietTTinChaoGiaReqList = new ArrayList<>();
}
