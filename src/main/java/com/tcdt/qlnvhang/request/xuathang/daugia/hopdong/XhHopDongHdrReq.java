package com.tcdt.qlnvhang.request.xuathang.daugia.hopdong;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.HhDviLquanReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhHopDongHdrReq extends BaseRequest {
    private Long id;

    private String soQdKq;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdKq;

    private String soQdPd;

    private String maDviTsan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianNkho;

    private String soHd;

    private String tenHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private String ghiChuNgayHluc;

    private String loaiHdong;

    private String ghiChuLoaiHdong;

    private Integer tgianThienHd;

    private Integer tgianBhanh;

    private String maDvi;

    private String diaChi;

    private String mst;

    private String tenNguoiDdien;

    private String chucVu;

    private String sdt;

    private String stk;

    private String tenNhaThau;

    private String diaChiNhaThau;

    private String mstNhaThau;

    private String tenNguoiDdienNhaThau;

    private String chucVuNhaThau;

    private String sdtNhaThau;

    private String stkNhaThau;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private Double soLuong;

    private Double donGia;

    private String trangThai;

    private List<XhHopDongDtlReq> children = new ArrayList<>();

}
