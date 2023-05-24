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

    public DcnbQuyetDinhDcCHdrDTO(Long id, String soQdinh ,LocalDate ngayKyQdinh) {
        this.id = id;
        this.soQdinh = soQdinh;
        this.ngayKyQdinh = ngayKyQdinh;
    }
}
