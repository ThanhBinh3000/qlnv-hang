package com.tcdt.qlnvhang.service.nhaphang.luongthucmuoi.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongFilterRequestDto;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

public interface QlpktclhPhieuKtChatLuongService {
	QlpktclhPhieuKtChatLuongResponseDto create (QlpktclhPhieuKtChatLuongRequestDto req) throws Exception;

	QlpktclhPhieuKtChatLuongResponseDto update (QlpktclhPhieuKtChatLuongRequestDto req) throws Exception;

	Page<QlpktclhPhieuKtChatLuongResponseDto> filter (QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception;

    BaseNhapHangCount count(QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception;

    Page<QlpktclhPhieuKtChatLuong> search (QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception;


    QlpktclhPhieuKtChatLuongResponseDto detail(Long id) throws Exception;

	boolean approve(StatusReq req) throws Exception;

	@Transactional(rollbackFor = Exception.class)
	boolean delete(Long id) throws Exception;

    boolean exportToExcel(QlpktclhPhieuKtChatLuongFilterRequestDto objReq, HttpServletResponse response) throws Exception;

	@Transactional
	boolean deleteMultiple(DeleteReq req) throws Exception;

	Integer getSo() throws Exception;
}
