package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinh;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdDchinhKhBdgReq extends BaseRequest {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    @ApiModelProperty(example = "2022")
    private  Integer namKh;

    private String maDvi;

    @Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "20-QD/TCDT")
    private String soQdPd;

    @Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "20-QD/TCDT")
    private String soQdDc;

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

    @Size(max = 20, message = "Căn cứ quyết định giao chỉ tiêu không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "20-QD/TCDT")
    private String soQdCc;

    private String tchuanCluong;

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

    private BigDecimal tongSoLuong;

    private BigDecimal tongTienKdienDonGia;

    private BigDecimal tongTienDatTruocDonGia;

    private Integer soDviTsan;

    private Integer slHdDaKy;

    private String ldoTuchoi;


   private String phanLoai;

    private List<XhQdPdKhBdgDtlReq> dsDeXuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdDen;

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();
}

