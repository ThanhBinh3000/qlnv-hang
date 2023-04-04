package com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhQdGiaoNvuXuatReq extends BaseRequest {
    private Long id;
    private Integer nam;

    private String maDvi;
    private String soQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;
    private String soHd;
    private LocalDate ngayKyHd;
    private String maDviTsan;
    private String tenTtcn;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String donViTinh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGnhan;
    private String trichYeu;
    private BigDecimal soLuong;

    private String bbTinhKho;

    private String bbHaoDoi;
    
    private List<XhQdGiaoNvuXuatCtReq> children = new ArrayList<>();

//    private List<Long> hopDongIds = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

    private String maChiCuc;
}
