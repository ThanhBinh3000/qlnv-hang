package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
public class HhQdGiaoNvuNhapKhacHdrReq {
    private Long id;
    private Long idQdPdNk;
    String soQd;
    String soQdPd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayQd;
    String maDvi;
    String trangThai;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayTao;
    String nguoiTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngaySua;
    String nguoiSua;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayPduyet;
    String nguoiPduyet;
    String loaiVthh;
    String cloaiVthh;
    String trichYeu;
    Integer nam;
    Long tongSlNhap;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date tgianNkMnhat;
    String tenDvi;
    String tenTrangThai;
    String tenTrangThaiNh;
    String tenLoaiVthh;
    String tenCloaiVthh;
    String loaiHinhNx;
    String kieuNx;
    private List<FileDinhKemReq> fileDinhKems;
    private List<FileDinhKemReq> fileCanCu;
}
