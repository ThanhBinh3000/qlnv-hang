package com.tcdt.qlnvhang.service.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.request.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongResponseDto;

public interface QlpktclhPhieuKtChatLuongService {
	QlpktclhPhieuKtChatLuongResponseDto create (QlpktclhPhieuKtChatLuongRequestDto req) throws Exception;
}
