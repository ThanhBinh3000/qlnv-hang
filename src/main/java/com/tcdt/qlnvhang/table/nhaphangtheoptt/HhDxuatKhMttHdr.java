package com.tcdt.qlnvhang.table.nhaphangtheoptt;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "HH_DX_KHMTT_HDR")
@Data
public class HhDxuatKhMttHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_HDR_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_HDR_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_HDR_SEQ")
    private Long id;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String loaiHinhNx;

    private String kieuNx;

    private String diaChi;

    private Integer namKh;

    private String soDxuat;

    private String trichYeu;

    private String tenDuAn;

    private Long idSoQdCc;

    private String soQdCc;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String ptMua;

    private String tchuanCluong;

    private String giaMua;

    private BigDecimal thueGtgt;

    @Temporal(TemporalType.DATE)
    private Date tgianMkho;

    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;

    private String ghiChu;

    private BigDecimal tongMucDt;

    private BigDecimal tongSoLuong;

    private BigDecimal tongTienGomThue;

    private String nguonVon;

    private Long maThop;

    private BigDecimal donGiaVat;

    private Long idSoQdPduyet;

    private String soQdPduyet;

    private String trangThaiTh;
    @Transient
    private String tenTrangThaiTh;
    @Transient
    private List<FileDinhKem> fileDinhKems;
    @Transient
    private List<HhDxuatKhMttSldd> children = new ArrayList<>();

    @Transient
    private List<HhDxuatKhMttCcxdg> ccXdgDtlList = new ArrayList<>();

    private String trangThai;

    @Transient
    private String tenTrangThai;


    @Temporal(TemporalType.DATE)
    private Date ngayTao;

    @Column(columnDefinition = "Number")
    private Long nguoiTaoId;

    @Transient
    private String tenNguoiTao;

    @Column(columnDefinition = "Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngaySua;

    @Column(columnDefinition = "Number")
    private Long nguoiSuaId;

    @Column(columnDefinition = "Number")
    private Long nguoiGuiDuyetId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayGuiDuyet;

    @Column(columnDefinition = "Number")
    private Long nguoiPduyetId;

    @Transient
    private String tenNguoiPduyet;

    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;

    private String lyDoTuChoi;

    public String getTenTrangThai() {
        return NhapXuatHangTrangThaiEnum.getTenById(trangThai);
    }


}
