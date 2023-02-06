package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;

@Data
public class SearchXhThopDxKhBtt extends BaseRequest {
    Integer namKh;
    String loaiVthh;
    String cloaiVthh;
    String noiDungThop;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayThopTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayThopDen;

    String TrangThai;
}
