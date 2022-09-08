package com.tcdt.qlnvhang.response.banhangdaugia.bienbanbandaugia;

import com.tcdt.qlnvhang.response.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BhBbBanDauGiaRes extends CommonResponse {
    private Long id;
    private String soBienBan;
    private String trichYeu;
    private LocalDate ngayKy;
    private String loaiVthh;
    private String maVatTuCha;
    private String tenVatTuCha;
    private Long thongBaoBdgId;
    private String maThongBaoBdg;
    private String donViThongBao;
    private LocalDate ngayToChucTu;
    private LocalDate ngayToChucDen;
    private String diaDiem;

    private Long qdPdKhBdgId;
    private String soQdPdKhBdg;

    private Long qdPdKqBdgId;
    private String soQdPdKqBdg;

    private String hinhThucDauGia;
    private String phuongThucDauGia;
    private List<BhBbBanDauGiaCtRes> cts = new ArrayList<>();
    private List<BhBbBanDauGiaCt1Res> ct1s = new ArrayList<>();
}
