package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtl;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class XhThopDxKhBdgReq extends XhThopChiTieuReq {
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
    private Long idQdPd;
    private String soQdPd;
    private String loaiHinhNx;
    private String kieuNx;
    private String trangThai;
    private LocalDate ngayGuiDuyet;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private List<XhThopDxKhBdgDtl> children = new ArrayList<>();
}
