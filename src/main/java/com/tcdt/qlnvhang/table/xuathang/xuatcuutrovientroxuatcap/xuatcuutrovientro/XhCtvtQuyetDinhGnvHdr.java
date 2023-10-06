package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtvtQuyetDinhGnvHdr.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
public class XhCtvtQuyetDinhGnvHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_QUYET_DINH_GNV_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtQuyetDinhGnvHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtQuyetDinhGnvHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtQuyetDinhGnvHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soBbQd;
  private LocalDate ngayKy;
  private Long idQdPd;
  private String soQdPd;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private BigDecimal soLuong;
  private LocalDate thoiGianGiaoNhan;
  private String trichYeu;
  private String trangThai;
  private String lyDoTuChoi;
  private String trangThaiXh;
  private String soBbHaoDoi;
  private String soBbTinhKho;
  private BigDecimal tongSoLuong;
  private BigDecimal thanhTien;
  private String type;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String loaiNhapXuat;
  private String kieuNhapXuat;
  private String mucDichXuat;
  private Long idLanhDao;

  @Transient
  private String tenDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenTrangThaiXh;


  @OneToMany(mappedBy = "xhCtvtQuyetDinhGnvHdr", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<XhCtvtQuyetDinhGnvDtl> dataDtl = new ArrayList<>();

  public void setDataDtl(List<XhCtvtQuyetDinhGnvDtl> children) {
    dataDtl.clear();
    if (!DataUtils.isNullOrEmpty(children)) {
      children.forEach(s -> {
        s.setXhCtvtQuyetDinhGnvHdr(this);
      });
      dataDtl.addAll(children);
    }
  }

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhCtvtQuyetDinhGnvHdr.TABLE_NAME + "_FILE_DINH_KEM'")
  private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

  public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
    this.fileDinhKem.clear();
    if (!DataUtils.isNullObject(fileDinhKem)) {
      fileDinhKem.forEach(s -> {
        s.setDataType(XhCtvtQuyetDinhGnvHdr.TABLE_NAME + "_FILE_DINH_KEM");
        s.setXhCtvtQuyetDinhGnvHdr(this);
      });
      this.fileDinhKem.addAll(fileDinhKem);
    }
  }

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhCtvtQuyetDinhGnvHdr.TABLE_NAME + "_CAN_CU'")
  private List<FileDinhKemJoinTable> canCu = new ArrayList<>();

  public void setCanCu(List<FileDinhKemJoinTable> fileDinhKem) {
    this.canCu.clear();
    if (!DataUtils.isNullObject(fileDinhKem)) {
      fileDinhKem.forEach(s -> {
        s.setDataType(XhCtvtQuyetDinhGnvHdr.TABLE_NAME + "_CAN_CU");
        s.setXhCtvtQuyetDinhGnvHdr(this);
      });
      this.canCu.addAll(fileDinhKem);
    }
  }

  public String getTrangThai() {
    setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
    return trangThai;
  }

  public String getTrangThaiXh() {
    setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(trangThaiXh));
    return trangThaiXh;
  }
  /*public XhCtvtQuyetDinhGnvHdr(LocalDateTime ngayTao, Long nguoiTaoId, LocalDateTime ngaySua, Long nguoiSuaId, Long id, String maDvi, Integer nam, String soBbQd, LocalDate ngayKy, Long idQdPd, String soQdPd, String loaiVthh, String cloaiVthh, String tenVthh, BigDecimal soLuong, LocalDate thoiGianGiaoNhan, String trichYeu, String trangThai, String lyDoTuChoi, String trangThaiXh, String soBbHaoDoi, String soBbTinhKho, BigDecimal tongSoLuong, BigDecimal thanhTien, String type, LocalDate ngayGduyet, Long nguoiGduyetId, LocalDate ngayPduyet, Long nguoiPduyetId, String loaiNhapXuat, String kieuNhapXuat, String mucDichXuat, String tenDvi, String tenLoaiVthh, String tenCloaiVthh, String tenTrangThai, String tenTrangThaiXh, List<XhCtvtQdGiaoNvXhDtl> noiDungCuuTro, List<XhCtvtQuyetDinhGnvDtl> children, List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem, List<FileDKemJoinHoSoKyThuatDtl> canCu) {
    super(ngayTao, nguoiTaoId, ngaySua, nguoiSuaId);
    this.id = id;
    this.maDvi = maDvi;
    this.nam = nam;
    this.soBbQd = soBbQd;
    this.ngayKy = ngayKy;
    this.idQdPd = idQdPd;
    this.soQdPd = soQdPd;
    this.loaiVthh = loaiVthh;
    this.cloaiVthh = cloaiVthh;
    this.tenVthh = tenVthh;
    this.soLuong = soLuong;
    this.thoiGianGiaoNhan = thoiGianGiaoNhan;
    this.trichYeu = trichYeu;
    this.trangThai = trangThai;
    this.lyDoTuChoi = lyDoTuChoi;
    this.trangThaiXh = trangThaiXh;
    this.soBbHaoDoi = soBbHaoDoi;
    this.soBbTinhKho = soBbTinhKho;
    this.tongSoLuong = tongSoLuong;
    this.thanhTien = thanhTien;
    this.type = type;
    this.ngayGduyet = ngayGduyet;
    this.nguoiGduyetId = nguoiGduyetId;
    this.ngayPduyet = ngayPduyet;
    this.nguoiPduyetId = nguoiPduyetId;
    this.loaiNhapXuat = loaiNhapXuat;
    this.kieuNhapXuat = kieuNhapXuat;
    this.mucDichXuat = mucDichXuat;
    this.tenDvi = tenDvi;
    this.tenLoaiVthh = tenLoaiVthh;
    this.tenCloaiVthh = tenCloaiVthh;
    this.tenTrangThai = tenTrangThai;
    this.tenTrangThaiXh = tenTrangThaiXh;
    this.noiDungCuuTro = noiDungCuuTro;
    this.children = children;
    this.fileDinhKem = fileDinhKem;
    this.canCu = canCu;
  }*/
}
