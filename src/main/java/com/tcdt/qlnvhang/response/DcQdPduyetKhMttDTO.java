package com.tcdt.qlnvhang.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttDx;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import com.tcdt.qlnvhang.table.HhQdPheduyetKqMttSLDD;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
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
public class DcQdPduyetKhMttDTO {
    private Long id;

    private Integer namKh;

    private String maDvi;
    private String tenDvi;

    private String soQd;
    private String soQdDc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayQd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private Long idThHdr;

    private String soTrHdr;

    private Long idTrHdr;

    private String trichYeu;

    private String loaiVthh;
    private String tenLoaiVthh;

    private String cloaiVthh;
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String tchuanCluong;

    private Boolean lastest;
    private Boolean isChange;

    private String phanLoai;

    private Long idGoc;
    private Long idSoQdCc;
    private String soQdCc;

    private Long idQdGnvu;
    private String soQdGnvu;

    private String ptMuaTrucTiep;
    private String tenPtMuaTrucTiep;

    private String trangThaiHd;

    private String tenTrangThaiHd;

    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    private List<HhQdPheduyetKhMttDx> children = new ArrayList<>();

    private Long soLanDieuChinh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyDc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdGoc;
    private String trichYeuDc;
    private Long idQdGoc;
    private String soQdGoc;
    private String trangThai;
    private String tenTrangThai;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;
    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private String nguoiSua;
    @Temporal(TemporalType.DATE)
    private Date ngayGduyet;
    private String nguoiGduyet;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyet;
    private String ldoTchoi;
    private String soToTrinh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoCv;
    private String loaiHinhNx;
    private String kieuNx;
    private String noiDungToTrinh;
    private String noiDungQdDc;
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();
    private List<FileDinhKem> cvanToTrinh = new ArrayList<>();
    private List<HhDcQdPduyetKhmttDx> hhDcQdPduyetKhmttDxList = new ArrayList<>();

}
