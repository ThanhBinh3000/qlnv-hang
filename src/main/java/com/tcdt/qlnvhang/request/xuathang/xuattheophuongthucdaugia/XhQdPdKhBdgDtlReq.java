package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdPdKhBdgDtlReq {
    private Long id;
    private Long idQdPd;
    private String maDvi;
    private String tenDvi;
    private String diaChi;
    private Long idDxuat;
    private String soDxuat;
    private String trichYeu;
    private String soQdCtieu;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String tchuanCluong;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private Integer soDviTsan;
    private Integer slHdDaKy;

    private List<XhQdPdKhBdgPlReq> xhQdPdKhBdgPlReq=new ArrayList<>();
}
