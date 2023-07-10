package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ScKiemTraChatLuongReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhnsId;
    private String maQhns;
    private String soPhieuKdcl;
    private LocalDate ngayLapPhieu;
    private String canBoTaoId;
    private String canBoTao;
    private String truongPhongKtbqId;
    private String truongPhongKqbq;
    private String soQdGiaoNvId;
    private String soQdGiaoNv;
    private String phieuXuatKhoId;
    private String soPhieuXuatKho;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String thuKhoId;
    private String thuKho;
    private String maLoaiHang;
    private String loaiHang;
    private String maChungLoaiHang;
    private String chungLoaiHang;
    private String dviKiemDinh;
    private LocalDate ngayKiemDinhMau;
    private String hinhThucBaoQuan;
    private String danhGia;
    private Boolean ketQua;
    private String nhanXet;
    private List<ScKiemTraChatLuongDtl> scKiemTraChatLuongDtls;
    private List<FileDinhKemReq> fileDinhKemReqs;
}
