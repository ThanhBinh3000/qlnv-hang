package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = DcnbDataLink.TABLE_NAME)
@Getter
@Setter
public class DcnbDataLink {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_DATA_LINK";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME +"_SEQ")
    @SequenceGenerator(sequenceName =  TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")
    private Long id;

    @Column(name = "KE_HOACH_DC_DTL_ID")
    private Long keHoachDcDtlId;

    @Column(name = "KE_HOACH_DC_HDR_ID")
    private Long keHoachDcHdrId;

    @Column(name = "KE_HOACH_DC_DTL_PARENT_ID")
    private Long keHoachDcDtlParentId;

    @Column(name = "KE_HOACH_DC_HDR_PARENT_ID")
    private Long keHoachDcHdrParentId;
    @Column(name = "QD_CC_ID")
    private Long qdCcId;
    @Column(name = "QD_CC_PARENT_ID")
    private Long qdCcParentId;
    @Column(name = "QD_CTC_ID")
    private Long qdCtcId;
    @Column(name = "XDC_BBLM_ID")
    private Long xdcBblmId;
    @Column(name = "NDC_BBLM_ID")
    private Long ndcBblmId;
    @Column(name = "XDC_PHIEU_KIEM_NGHIEM_ID")
    private Long xdcPhieuKiemNghiemId;
    @Column(name = "PHIEU_XUAT_KHO_ID")
    private Long phieuXuatKhoId;
    @Column(name = "XDC_BK_CAN_HANG_ID")
    private Long xdcBkCanHangId;
    @Column(name = "BB_TINH_KHO_ID")
    private Long bbTinhKhoId;
    @Column(name = "BB_HAO_DOI_ID")
    private Long bbHaoDoiId;
    @Column(name = "BB_NGHIEM_THU_ID")
    private Long bbNghiemThuId;
    @Column(name = "PHIEU_KIEM_TRA_ID")
    private Long phieuKiemTraId;
    @Column(name = "NDC_PHIEU_KIEM_NGHIEM_ID")
    private Long ndcPhieuKiemNghiemId;
    @Column(name = "BB_CHUAN_BI_KHO_ID")
    private Long bbChuanBiKhoId;
    @Column(name = "PHIEU_NHAP_KHO_ID")
    private Long phieuNhapKhoId;
    @Column(name = "NDC_BK_CAN_HANG_ID")
    private Long ndcBkCanHangId;
    @Column(name = "BB_NHAP_DAY_KHO_ID")
    private Long bbNhapDayKhoId;
    @Column(name = "BK_NHAP_VT_ID")
    private Long bkNhapVtId;
    @Column(name = "BB_KET_THUC_NK_ID")
    private Long bkKetThucNkId;
    @Column(name = "BB_GIAO_NHAN_ID")
    private Long bbGiaoNhanId;
}
