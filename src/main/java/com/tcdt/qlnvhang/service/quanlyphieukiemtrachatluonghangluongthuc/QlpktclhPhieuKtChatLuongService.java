package com.tcdt.qlnvhang.service.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongFilterRequestDto;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongResponseDto;
import org.springframework.data.domain.Page;

public interface QlpktclhPhieuKtChatLuongService {
	QlpktclhPhieuKtChatLuongResponseDto create (QlpktclhPhieuKtChatLuongRequestDto req) throws Exception;

	QlpktclhPhieuKtChatLuongResponseDto update (QlpktclhPhieuKtChatLuongRequestDto req) throws Exception;

	Page<QlpktclhPhieuKtChatLuongResponseDto> filter (QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception;

	Page<QlpktclhPhieuKtChatLuong> timKiem (QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception;


}
