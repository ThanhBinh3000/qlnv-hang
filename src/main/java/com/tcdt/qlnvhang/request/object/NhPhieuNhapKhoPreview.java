package com.tcdt.qlnvhang.request.object;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt1;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NhPhieuNhapKhoPreview {
    private Long id;
    private String soPhieuKtraCl;
    private String soBienBanGuiHang;
    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr
    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr
    private String soPhieuNhapKho;
    private String soHd;
    private Date ngayHd;
    private Date ngayNhapKho;
    private Date thoiGianGiaoNhan;
    private Date ngayTaoPhieu;
    private BigDecimal taiKhoanNo;
    private BigDecimal taiKhoanCo;
    private String loaiHinhNhap;
    private String tenNguoiTao;
    private Long idDdiemGiaoNvNh;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String capDvi;
    private Integer so;
    private Integer nam;
    private Long hoSoKyThuatId;
    private BigDecimal tongSoLuong;
    private BigDecimal tongSoTien;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private Integer soNo; // Số nợ
    private Integer soCo; // Số có
    private String nguoiGiaoHang;
    private String cmtNguoiGiaoHang;
    private String donViGiaoHang;
    private String diaChiNguoiGiao;
    private String keToanTruong;
    private String ghiChu;
    private List<NhPhieuNhapKhoCt> hangHoaList = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    private List<NhPhieuNhapKhoCt1> chiTiet1s = new ArrayList<>();
    private NhBangKeCanHang bangKeCanHang;
    private BigDecimal soLuongNhapKho;
    private BigDecimal tongChungTu;
    private BigDecimal tongThucNhap;
    private BigDecimal tongThanhTien;
}
