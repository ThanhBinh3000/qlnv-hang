package com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdNvXhBttHdrReq extends BaseRequest {
    private Long id;

    private String maDvi;

    private Integer namKh;

    private String soQdNv;

    private Long idHd;

    private String soHd;

    private Long idQdPd;

    private Long idQdPdDtl;

    private String soQdPd;

    private String maDviTsan;

    private String tenTccn;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String loaiHinhNx;

    private String kieuNx;

    private BigDecimal soLuongBanTrucTiep;

    private String donViTinh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGnhan;

    private String trichYeu;

    private String trangThaiXh;

    private String phanLoai;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyHd;

    private List<XhQdNvXhBttDtlReq> children = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoDen;

    private String maChiCuc;

    private String soBienBan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLayMauTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLayMauDen;
}

