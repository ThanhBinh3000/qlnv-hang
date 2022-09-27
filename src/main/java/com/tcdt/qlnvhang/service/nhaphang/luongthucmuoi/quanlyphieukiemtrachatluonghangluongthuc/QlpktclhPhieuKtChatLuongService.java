package com.tcdt.qlnvhang.service.nhaphang.luongthucmuoi.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongFilterRequestDto;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongResponseDto;
import com.tcdt.qlnvhang.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public interface QlpktclhPhieuKtChatLuongService extends BaseService<QlpktclhPhieuKtChatLuong,QlpktclhPhieuKtChatLuongRequestDto,Long> {


//    boolean export(QlpktclhPhieuKtChatLuongFilterRequestDto objReq) throws Exception;
//	QlpktclhPhieuKtChatLuong create (QlpktclhPhieuKtChatLuongRequestDto req) throws Exception;
//
//	QlpktclhPhieuKtChatLuong update (QlpktclhPhieuKtChatLuongRequestDto req) throws Exception;
//
//	Page<QlpktclhPhieuKtChatLuongResponseDto> filter (QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception;
//
//    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
//
//    Page<QlpktclhPhieuKtChatLuong> search (QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception;
//
//
//    QlpktclhPhieuKtChatLuong detail(Long id) throws Exception;
//
//	boolean approve(StatusReq req) throws Exception;
//
//	@Transactional(rollbackFor = Exception.class)
//	void delete(Long id) throws Exception;
//
//    boolean exportToExcel(QlpktclhPhieuKtChatLuongFilterRequestDto objReq, HttpServletResponse response) throws Exception;
//
//	@Transactional
//	boolean deleteMultiple(DeleteReq req) throws Exception;
//
//	Integer getSo() throws Exception;
}
