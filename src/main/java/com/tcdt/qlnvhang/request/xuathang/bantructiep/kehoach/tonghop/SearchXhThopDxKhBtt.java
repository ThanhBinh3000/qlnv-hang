package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SearchXhThopDxKhBtt extends BaseRequest {
   private Long id;
   private LocalDate ngayThop;
   private String loaiVthh;
   private String cloaiVthh;
   private LocalDate ngayDuyetTu;
   private LocalDate ngayDuyetDen;
   private String noiDungThop;
   private Integer namKh;
   private String maDvi;
   private String trangThai;
   private LocalDateTime ngayThopTu;
   private LocalDateTime ngayThopDen;
   private String dvql;
}
