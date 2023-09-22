package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DcnbQuyetDinhDcCHdrDTO {
    private Long id;
    private String soQdinh;
    private LocalDate ngayKyQdinh;
    private LocalDate ngayHieuLuc;

    public DcnbQuyetDinhDcCHdrDTO(Long id, String soQdinh ,LocalDate ngayKyQdinh,LocalDate ngayHieuLuc) {
        this.id = id;
        this.soQdinh = soQdinh;
        this.ngayKyQdinh = ngayKyQdinh;
        this.ngayHieuLuc =ngayHieuLuc;
    }
}
