package com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = XhKqBttHdr.TABLE_NAME)
@Data
public class XhKqBttHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_KQ_BTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_KQ_BTT_HDR_SEQ")
    @SequenceGenerator(sequenceName = "XH_KQ_BTT_HDR_SEQ", allocationSize = 1, name = "XH_KQ_BTT_HDR_SEQ")

    private Long id;

    private Long idPdKhDtl;

    private Long idPdKhHdr;

    private Integer namKh;

    private String soQdKq;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayKy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayHluc;

    private String soQdPd;

    private String trichYeu;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String diaDiemChaoGia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayKthuc;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String ghiChu;

    private String pthucBanTrucTiep;

    private String trangThaiHd;
    @Transient
    private String tenTrangThaiHd;

    private String trangThaiXh;
    @Transient
    private String tenTrangThaiXh;



    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private FileDinhKem fileDinhKem;

    @Transient
    private List<XhHopDongBttHdr> listHopDongBtt;

    @Transient
    private List<XhKqBttDtl> children = new ArrayList<>();

}
