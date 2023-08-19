package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatDtl;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapHdr;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtvtQuyetDinhPdHdr.TABLE_NAME)
@Getter
@Setter
public class XhCtvtQuyetDinhPdHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_QUYET_DINH_PD_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtQuyetDinhPdHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;
  private LocalDate ngayHluc;
  private Long idTongHop;
  private String maTongHop;
  private LocalDate ngayThop;
  private Long idDx;
  private String soDx;
  private Long idXc;
  private String soXc;
  private LocalDate ngayDx;
  private Long tongSoLuongDx;
  private Long tongSoLuong;
  private Long thanhTien;
  private Long soLuongXuaCap;
  private String loaiVthh;
  private String cloaiVthh;
  private String loaiNhapXuat;
  private String kieuNhapXuat;
  private String mucDichXuat;
  private String trichYeu;
  private String trangThai;
  private String lyDoTuChoi;
  private boolean xuatCap = false;
  private String type;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String donViTinh;

  private Long idQdGiaoNv;
  private String soQdGiaoNv;


  @Transient
  private String tenDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;

  @OneToMany(mappedBy = "xhCtvtQuyetDinhPdHdr", cascade = CascadeType.ALL,orphanRemoval = true)
  private List<XhCtvtQuyetDinhPdDtl> quyetDinhPdDtl = new ArrayList<>();
  public void setQuyetDinhPdDtl(List<XhCtvtQuyetDinhPdDtl> data) {
    quyetDinhPdDtl.clear();
    if (!DataUtils.isNullOrEmpty(data)) {
      data.forEach(s -> {
        s.setXhCtvtQuyetDinhPdHdr(this);
      });
      quyetDinhPdDtl.addAll(data);
    }
  }

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_FILE_DINH_KEM'")
  private List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem = new ArrayList<>();
  public void setFileDinhKem(List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem) {
    this.fileDinhKem.clear();
    if (!DataUtils.isNullObject(fileDinhKem)) {
      fileDinhKem.forEach(s -> {
        s.setDataType(XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_FILE_DINH_KEM");
        s.setXhCtvtQuyetDinhPdHdr(this);
      });
      this.fileDinhKem.addAll(fileDinhKem);
    }
  }

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU'")
  private List<FileDKemJoinHoSoKyThuatDtl> canCu = new ArrayList<>();
  public void setCanCu(List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem) {
    this.canCu.clear();
    if (!DataUtils.isNullObject(fileDinhKem)) {
      fileDinhKem.forEach(s -> {
        s.setDataType(XhCtvtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU");
        s.setXhCtvtQuyetDinhPdHdr(this);
      });
      this.canCu.addAll(fileDinhKem);
    }
  }

  public String getTrangThai() {
    setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
    return trangThai;
  }
}
