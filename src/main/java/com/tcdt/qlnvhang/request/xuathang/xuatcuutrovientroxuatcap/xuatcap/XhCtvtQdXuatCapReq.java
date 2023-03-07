package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhCtvtQdXuatCapReq {
    private Long id;
    private Long qdPaXuatCapId;
    @NotNull(message = "Không được để trống")
    private Integer nam;
    @NotNull(message = "Không được để trống")
    private String soQd;
    @NotNull(message = "Không được để trống")
    private LocalDate ngayKy;
    @NotNull(message = "Không được để trống")
    private LocalDate ngayHluc;
    private String trichYeu;
    private List<FileDinhKemReq> fileDinhKem;
    private List<FileDinhKemReq> canCu = new ArrayList<>();
    private String loaiVthh;
    private String loaiNhapXuat;
    private String kieuNhapXuat;
    private LocalDate thoiHanXuat;
    private Long tongSoLuongThoc;
    private Long thanhTien;
    private List<XhCtvtQdXuatCapDtlReq> deXuatPhuongAn = new ArrayList<>();
}
