package com.tcdt.qlnvhang.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttDx;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import com.tcdt.qlnvhang.table.HhQdPheduyetKqMttSLDD;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBcanKeHangHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanDayKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhangDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class HopDongMttHdrDTO {
    private Long id;
    private Integer namNhap;
    private String soQd;
    @Temporal(TemporalType.DATE)
    private Date ngayQd;
    @Temporal(TemporalType.DATE)
    private Date ngayKyHd;
    private String maDvi;
    private String tenDvi;
    private String loaiQd;
    private Long idHd;
    private String soHd;
    private String tenHd;
    private Long idQdPdKh;
    private String soQdPdKh;
    private Long idQdPdKq;
    private String soQdPdKq;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String donViTinh;
    @Temporal(TemporalType.DATE)
    private Date tgianNkho;
    private String trichYeu;
    private String trangThai;
    private String tenTrangThai;
    private BigDecimal soLuong;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;
    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private  String nguoiSua;
    private String ldoTuchoi;
    @Temporal(TemporalType.DATE)
    private Date ngayGduyet;
    private String nguoiGduyet;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyet;
    private List<HopDongMttHdr> hopDongMttHdrs = new ArrayList<>();
    private HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr;
    private List<HhQdGiaoNvNhangDtl> hhQdGiaoNvNhangDtlList= new ArrayList<>();
    private List<HhPhieuNhapKhoHdr> hhPhieuNhapKhoHdrList = new ArrayList<>();
    private List<HhBcanKeHangHdr> hhBcanKeHangHdrList = new ArrayList<>();
    private List<HhBienBanDayKhoHdr> hhBienBanDayKhoHdrList = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    private FileDinhKem fileDinhKem;






    private Long idPdKhDtl;
    private Long idPdKhHdr;
    private Integer namKh;
    private String soQdKq;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;
    private String diaDiemChaoGia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayMkho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayMua;
    private String ghiChu;
    private String trangThaiHd;
    private String tenTrangThaiHd;
    private String trangThaiNh;
    private String tenTrangThaiNh;
    private List<HopDongMttHdr> hopDongMttHdrList = new ArrayList<>();
    private List<HhQdPheduyetKqMttSLDD> danhSachCtiet = new ArrayList<>();
}
