package com.tcdt.qlnvhang.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhChiTietTTinChaoGiaReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhQdPheduyetKhMttHdrReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    //	@NotNull(message = "Không được để trống")
    @Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "00")
    private String loaiVthh;

    private String cloaiVthh;

    //	@NotNull(message = "Không được để trống")
    @ApiModelProperty(example = "2022")
    private Integer namKh;

    //	@NotNull(message = "Không được để trống")
    @Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "20-QD/TCDT")
    private String soQd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayQd;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    @ApiModelProperty(example = "Ghi chú")
    private String ghiChu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private String trichYeu;

    private String soTrHdr;

    private String phanLoai;

    private Boolean lastest = false;

    private Long idThHdr;
    private Long idTrHdr;

    private BigDecimal donGiaVat;

    private String maDvi;


    private String maThop;
    private String trangThai;
    private String trangThaiTkhai;
    private String pthucMuatt;
    private String diaDiemCgia;

    private String moTaHangHoa;
    private String ptMua;
    private String giaMua;
    private BigDecimal donGia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianMkho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKthuc;

    private String soQdPdCg;
    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();

    private List<HhQdPheduyetKhMttDxReq> dsDiaDiem = new ArrayList<>();

    private List<HhChiTietTTinChaoGiaReq> hhChiTietTTinChaoGiaReqList = new ArrayList<>();
}
