package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhKqBttHdrReq extends BaseRequest  {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idPdKhDtl;

    private Long idPdKhHdr;

    private Integer namKh;

    private String soQdKq;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayKy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayHluc;

    private String soQdPd;

    private String trichYeu;

    private String maDvi;

    private String diaDiemChaoGia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayKthuc;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String ghiChu;

    private String pthucBanTrucTiep;

    private String loaiHinhNx;

    private String kieuNx;

    @Transient
    private List<FileDinhKemReq> fileCanCu = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileQdDaKy = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileQd = new ArrayList<>();

    @Transient
    private List<XhKqBttDtlReq> children = new ArrayList<>();

    private String maChiCuc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date  ngayCgiaTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date  ngayCgiaDen;

    private String trangThaiHd;
}
