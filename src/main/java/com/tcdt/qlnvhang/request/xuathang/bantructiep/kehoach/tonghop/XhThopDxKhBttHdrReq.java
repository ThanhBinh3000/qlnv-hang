package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtl;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class XhThopDxKhBttHdrReq extends XhThopChiTieuReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idTh;
    private LocalDate ngayThop;
    private String loaiVthh;
    private String cloaiVthh;
    private LocalDate ngayDuyetTu;
    private LocalDate ngayDuyetDen;
    private String noiDungThop;
    private Integer namKh;
    private String maDvi;
    private Long idSoQdPd;
    private String soQdPd;
    private String trangThai;
    private String ghiChu;
    private LocalDate ngayGuiDuyet;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private List<XhThopDxKhBttDtl> children = new ArrayList<>();
}
