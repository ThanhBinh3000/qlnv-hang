package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntDsgthauReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdPdKhBdgDtlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idHdr;

    @NotNull(message = "Không được để trống")
    @Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
    @ApiModelProperty(example = "HNO")
    String maDvi;


    @NotNull(message = "Không được để trống")
    @Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
    @ApiModelProperty(example = "Cục Hà Nội")
    String tenDvi;

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

    @NotNull(message = "Không được để trống")
    @Size(max = 4, message = "Năm kế hoạch không được vượt quá 4 ký tự")
    @ApiModelProperty(example = "2022")
    String namKh;

    private Long idDxHdr;

    private String diaChi;

    private String trichYeu;

    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String tchuanCluong;


    private String soQdCtieu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienDen;
    private String loaiHdong;
    private Integer tgianKyHdong;
    private Integer tgianTtoan;
    private Integer tgianGnhan;
    private String tgianKyHdongGhiChu;
    private String tgianTtoanGhiChu;
    private String tgianGnhanGhiChu;
    private String thongBaoKh;
    private BigDecimal khoanTienDatTruoc;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienKdiem;
    private BigDecimal tongTienDatTruoc;


    private Integer soDviTsan;
    private Integer slHdDaKy;


    private List<XhQdPdKhBdgPlReq> dsGoiThau;

    private List<XhQdPdKhBdgPlReq> children;
}
