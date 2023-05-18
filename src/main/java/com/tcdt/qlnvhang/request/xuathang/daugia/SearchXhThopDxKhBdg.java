package com.tcdt.qlnvhang.request.xuathang.daugia;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SearchXhThopDxKhBdg extends BaseRequest {

   private Integer namKh;

   private String loaiVthh;

   private String cloaiVthh;

   private String noiDungThop;

   private LocalDate ngayThopTu;

   private LocalDate ngayThopDen;

   private String TrangThai;

   private String typeLoaiVthh;
}
