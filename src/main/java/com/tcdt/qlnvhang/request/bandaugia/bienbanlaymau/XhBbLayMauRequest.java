package com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhBbLayMauRequest extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String loaiBienBan;
    private String maQhNs;
    private String soBbLayMau;
    private LocalDate ngayLayMau;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String toChucCaNhan;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private Long idThuKho;
    private Long idKtvBaoQuan;
    private String truongBpKtbq;
    private Long idLanhDaoChiCuc;
    private String donViKnghiem;
    private String diaDiemLayMau;
    private BigDecimal soLuongKiemTra;
    private String donViTinh;
    private String chiTieuKiemTra;
    private Boolean ketQuaNiemPhong;
    private String trangThai;
    private Long idHaoDoi;
    private String soBbHaoDoi;
    private Long idTinhKho;
    private String soBbTinhKho;
    private LocalDate ngayXuatDocKho;
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileNiemPhong = new ArrayList<>();
    private List<XhBbLayMauCtRequest> children = new ArrayList<>();
    private String dvql;
    private LocalDate ngayLayMauTu;
    private LocalDate ngayLayMauDen;
    private String chiTieuChatLuong;
    private String phuongPhapLayMau;
    private String maDviCha;
}
