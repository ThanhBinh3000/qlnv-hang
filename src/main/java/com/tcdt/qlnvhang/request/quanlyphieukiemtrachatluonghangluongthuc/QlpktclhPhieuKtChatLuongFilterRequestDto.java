package com.tcdt.qlnvhang.request.quanlyphieukiemtrachatluonghangluongthuc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QlpktclhPhieuKtChatLuongFilterRequestDto {

	private Long soPhieu;

	private LocalDate ngayKiemTraTuNgay;

	private LocalDate ngayKiemTraDenNgay;

	private Long maNganKho;

	private Long maHangHoa;

	private String maDonVi;

	private Pageable pageable;

}
