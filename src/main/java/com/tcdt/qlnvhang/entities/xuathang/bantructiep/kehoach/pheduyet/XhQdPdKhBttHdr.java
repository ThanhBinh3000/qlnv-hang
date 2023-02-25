package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BTT_HDR")
@Data
public class XhQdPdKhBttHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BTT_HDR_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BTT_HDR_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BTT_HDR_SEQ")
    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String soQdPd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHluc;

    private Long idThHdr;

    private String soTrHdr;

    private Long idTrHdr;

    private String trichYeu;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String  moTaHangHoa;

    private String tchuanCluong;

    private Boolean lastest;

    private String phanLoai;

    private Long idGoc;

    private String maDviTsan;

    private String soHopDong;

    private String soQdCc;

    @Transient
    private List<XhQdPdKhBttDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();

    @Transient
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();



    // trường của chào giá, ủy quyền, mua lẻ.
    private String trangThaiChaoGia;
    @Transient
    private String tenTrangThaiChaoGia;

    private String pthucBanTrucTiep;

    private String diaDiemChaoGia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhanCgia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKthuc;

    private String ghiChu;


    private String soQdKq;

    @Transient
    private List<FileDinhKem> fileDinhKemUyQuyen = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKemMuaLe = new ArrayList<>();

    @Transient
    private List<XhTcTtinBtt> xhTcTtinBttList = new ArrayList<>();

    public String getTenTrangThai() {
        return NhapXuatHangTrangThaiEnum.getTenById(this.getTrangThai());
    }



}
