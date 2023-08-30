package com.tcdt.qlnvhang.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.bienbanlaymau.XhBienBanLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.hosokythuat.XhHoSoKyThuatDtl;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl.XhPhieuKnclHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdHdr;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "FILE_DINH_KEM")
@Data
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
  private XhQdDchinhKhBdgHdr xhQdDchinhKhBdgHdr;

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
  private XhQdDchinhKhBttHdr xhQdDchinhKhBttHdr;

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
}