package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QlhdmvtTtDonViCcResponseDTO {
    private Long id;
    private String tenDvThucHien;
    private String diaChi;
    private String maSoThue;
    private String soDienThoai;
    private String soTaiKhoan;
    private String tenNguoiDaiDien;
    private String chucVu;
}
