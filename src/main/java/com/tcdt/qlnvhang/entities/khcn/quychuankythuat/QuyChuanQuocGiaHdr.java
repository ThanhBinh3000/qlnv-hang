package com.tcdt.qlnvhang.entities.khcn.quychuankythuat;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxuatCuuTro;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.XhDxCuuTroDtl;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = QuyChuanQuocGiaHdr.TABLE_NAME)
@Data
public class QuyChuanQuocGiaHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "KHCN_QUY_CHUAN_QG_HDR";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = QuyChuanQuocGiaHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = QuyChuanQuocGiaHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = QuyChuanQuocGiaHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String soVanBan;
    private String idVanBanThayThe;
    private String soVanBanThayThe;
    private String maDvi;
    private String loaiVthh;
    private String cloaiVthh;
    private LocalDate ngayKy;
    private LocalDate ngayHieuLuc;
    private LocalDate ngayHetHieuLuc;
    private String soHieuQuyChuan;
    private String apDungTai;
    private String loaiApDung;
    private String danhSachApDung;
    private String trichYeu;
    private String type;
    private int thoiGianLuuKhoToiDa;
    private Boolean lastest;
    private Boolean apDungCloaiVthh;
    private String trangThai;
    private String trangThaiHl;
    private String listTenLoaiVthh;
    private Boolean isMat;
    private String maBn;


    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTrangThaiTh;
    @Transient
    private String tenLoaiHinhNhapXuat;
    @Temporal(TemporalType.DATE)
    private Date ngayGduyet;
    private Long nguoiGduyetId;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private Long nguoiPduyetId;
    private String ldoTuChoi;
    @Transient
    private List<QuyChuanQuocGiaDtl> tieuChuanKyThuat = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private String tenBn;

    @Transient
    String tenTrangThaiHl;

    public String getTenTrangThai() {
        return TrangThaiAllEnum.getLabelById(trangThai);
    }

    public String getTrangThaiHl() {
        LocalDate dateNow = LocalDate.now();
        if (getTrangThai().equals(TrangThaiAllEnum.BAN_HANH.getId())) {
            if (ObjectUtils.isEmpty(getNgayHieuLuc()) || (!ObjectUtils.isEmpty(getNgayHetHieuLuc()) && getNgayHetHieuLuc().isBefore(dateNow))) {
                return "00";
            } else if ((!ObjectUtils.isEmpty(getNgayHieuLuc()) && getNgayHieuLuc().isAfter(dateNow))) {
                return "02";
            }
            return "01";
        } else {
            if ((!ObjectUtils.isEmpty(getNgayHetHieuLuc()) && getNgayHetHieuLuc().isBefore(dateNow))) {
                return "00";
            } else {
                return "02";
            }
        }
    }
}
