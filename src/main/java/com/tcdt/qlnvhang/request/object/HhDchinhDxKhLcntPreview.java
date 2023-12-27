package com.tcdt.qlnvhang.request.object;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HhDchinhDxKhLcntPreview {
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private Integer namKhoach;
    private String soQdPdKhlcnt;
    private String ngayPdKhlcnt;
    private String soQdDcKhlcnt;
    private String ngayPdDcKhlcnt;
    private String tongSl;
    private String tongSlDc;
    private String tongThanhTien;
    private List<HhDchinhDxKhLcntDtlPreview> children;
}
