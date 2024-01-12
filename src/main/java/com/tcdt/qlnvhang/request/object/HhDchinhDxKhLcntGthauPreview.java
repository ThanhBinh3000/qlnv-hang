package com.tcdt.qlnvhang.request.object;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HhDchinhDxKhLcntGthauPreview {
    private String gthau;
    private String soLuong;
    private String soLuongDc;
    private String tenCloaiVthh;
    private String giaGthau;
    private Integer tgianThienHd;
    private Integer tgianThienHdDc;
    private List<HhDchinhDxKhLcntGthauCtietPreview> children;
}
