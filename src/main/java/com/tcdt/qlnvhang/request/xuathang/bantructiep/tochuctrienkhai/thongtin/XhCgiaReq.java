package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhCgiaReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idDtl;

    private Long idQdPdDtl;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private String pthucBanTrucTiep;

    private String diaDiemChaoGia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKthuc;

    private String ghiChu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thoiHanBan;



    private List<FileDinhKemReq> fileDinhKemUyQuyen = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKemMuaLe = new ArrayList<>();

    private List<XhQdPdKhBttDviReq> children = new ArrayList<>();
}
