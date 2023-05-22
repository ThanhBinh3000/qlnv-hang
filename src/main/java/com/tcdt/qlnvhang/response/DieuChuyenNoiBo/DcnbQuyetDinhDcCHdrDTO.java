package com.tcdt.qlnvhang.response.DieuChuyenNoiBo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DcnbQuyetDinhDcCHdrDTO {
    private Long id;
    private String soQdinh;
    private LocalDate ngayKyQdinh;
    private String loaiVthh;
    private String tenLoaiVthh;

    public DcnbQuyetDinhDcCHdrDTO(Long id, String soQdinh ,LocalDate ngayKyQdinh,String loaiVthh, String tenLoaiVthh) {
        this.id = id;
        this.soQdinh = soQdinh;
        this.ngayKyQdinh = ngayKyQdinh;
        this.loaiVthh = loaiVthh;
        this.tenLoaiVthh = tenLoaiVthh;
    }
}
