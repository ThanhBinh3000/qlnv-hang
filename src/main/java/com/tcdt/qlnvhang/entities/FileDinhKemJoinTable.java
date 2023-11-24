package com.tcdt.qlnvhang.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbHaoDoiHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.bienbanlaymau.XhBienBanLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.hosokythuat.XhHoSoKyThuatDtl;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl.XhPhieuKnclHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBangKeHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdHdr;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "FILE_DINH_KEM")
@Getter
@Setter
public class FileDinhKemJoinTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_DINH_KEM_SEQ")
    @SequenceGenerator(sequenceName = "FILE_DINH_KEM_SEQ", allocationSize = 1, name = "FILE_DINH_KEM_SEQ")
    Long id;
    String fileName;
    String fileSize;
    String fileUrl;
    String fileType;
    String dataType;
    String noiDung;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhDxKhBanDauGia xhDxKhBanDauGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhDxKhBanTrucTiepHdr xhDxKhBanTrucTiepHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
//  @JsonBackReference
    @JsonIgnore
    private XhHoSoKyThuatDtl parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhTlToChucHdr xhTlToChucHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhTlQuyetDinhHdr xhTlQuyetDinhHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhTlQuyetDinhPdKqHdr xhTlQuyetDinhPdKqHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhBienBanLayMauHdr xhBienBanLayMauHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhTlHopDongHdr xhTlHopDongHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhTlQdGiaoNvHdr xhTlQdGiaoNvHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhTlXuatKhoHdr xhTlXuatKhoHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhTlTinhKhoHdr xhTlTinhKhoHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhTlHaoDoiHdr xhTlHaoDoiHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhPhieuKnclHdr xhPhieuKnclHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhCtvtDeXuatHdr xhCtvtDeXuatHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhCtvtQuyetDinhPdHdr xhCtvtQuyetDinhPdHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhKqBttHdr xhKqBttHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhCtvtQuyetDinhGnvHdr xhCtvtQuyetDinhGnvHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhThTongHopHdr xhThTongHopHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhTcTtinBdgHdr xhTcTtinBdgHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhKqBdgHdr xhKqBdgHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhHopDongBttHdr xhHopDongBttHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhQdNvXhBttHdr xhQdNvXhBttHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhHopDongHdr xhHopDongHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhQdGiaoNvXh xhQdGiaoNvXh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhBbLayMau xhBbLayMau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhPhieuKnghiemCluong xhPhieuKnghiemCluong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhDgPhieuXuatKho xhDgPhieuXuatKho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhDgBangKeHdr xhDgBangKeHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhDgBbTinhKhoHdr xhDgBbTinhKhoHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhDgBbHaoDoiHdr xhDgBbHaoDoiHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhBbLayMauBttHdr xhBbLayMauBttHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhPhieuKtraCluongBttHdr xhPhieuKtraCluongBttHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhPhieuXkhoBtt xhPhieuXkhoBtt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhBkeCanHangBttHdr xhBkeCanHangBttHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhBbTinhkBttHdr xhBbTinhkBttHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhBbHdoiBttHdr xhBbHdoiBttHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataId", insertable = false, updatable = false)
    @JsonIgnore
    private XhCtvtBangKeHdr xhCtvtBangKeHdr;
}