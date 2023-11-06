package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_DC_QD_PDUYET_KHMTT_HDR")
@Data
public class HhDcQdPduyetKhmttHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DC_QD_PDUYET_KHMTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DC_QD_PDUYET_KHMTT_SEQ")
    @SequenceGenerator(sequenceName = "HH_DC_QD_PDUYET_KHMTT_SEQ", allocationSize = 1, name="HH_DC_QD_PDUYET_KHMTT_SEQ")

    private Long id;
    private String soQdDc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyDc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdGoc;
    private String trichYeu;
    private Long idQdGoc;
    private String soQdGoc;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private Integer namKh;
    private String maDvi;
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
    private Long soLanDieuChinh;
    private Long idSoQdCc;
    private String soQdCc;
    private String soToTrinh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoCv;
    private String loaiHinhNx;
    private String kieuNx;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();
    @Transient
    private List<FileDinhKem> cvanToTrinh = new ArrayList<>();

    @Transient
    private List<HhDcQdPduyetKhmttDx> hhDcQdPduyetKhmttDxList=new ArrayList<>();
}
