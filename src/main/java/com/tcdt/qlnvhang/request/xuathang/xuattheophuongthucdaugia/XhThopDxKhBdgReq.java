package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgDtl;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhThopDxKhBdgReq extends XhThopChiTieuReq{
    private Long id;
    private String maThop;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayThop;
    private String maDvi;
    private Integer namKh;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String loaiHdong;
    private String noiDungThop;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienDen;
    private String trangThai;
    private String soQdPd;

    @Transient
    List<XhThopDxKhBdgDtl> thopDxKhBdgDtlList= new ArrayList<>();
}
