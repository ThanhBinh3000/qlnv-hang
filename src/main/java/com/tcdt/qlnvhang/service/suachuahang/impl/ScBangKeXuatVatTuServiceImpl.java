package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScBangKeXuatVatTuDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScBangKeXuatVatTuHdrRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeXuatVatTuDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeXuatVatTuHdr;
import com.tcdt.qlnvhang.util.UserUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScBangKeXuatVatTuServiceImpl extends BaseServiceImpl {
    @Autowired
    private ScBangKeXuatVatTuHdrRepository hdrRepository;

    @Autowired
    private ScBangKeXuatVatTuDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<ScBangKeXuatVatTuHdr> searchPage(CustomUserDetails currentUser, ScBangKeXuatVatTuReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScBangKeXuatVatTuHdr> searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Transactional
    public ScBangKeXuatVatTuHdr create(ScBangKeXuatVatTuReq objReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        String dvql = userInfo.getDvql();
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
        if (optional.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
            throw new Exception("Số bảng kê đã tồn tại");
        }
        ScBangKeXuatVatTuHdr data = new ScBangKeXuatVatTuHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(dvql);
        data.setTenDvi(userInfo.getTenDvi());
        objReq.getChildren().forEach(e -> e.setScBangKeXuatVatTuHdr(data));
        ScBangKeXuatVatTuHdr created = hdrRepository.save(data);

        return created;
    }

    @Transactional
    public ScBangKeXuatVatTuHdr update(ScBangKeXuatVatTuReq objReq) throws Exception {
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<ScBangKeXuatVatTuHdr> soDxuat = hdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
        if (StringUtils.isNotEmpty(objReq.getSoBangKe())) {
            if (soDxuat.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
                if (!soDxuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("số bảng kê đã tồn tại");
                }
            }
        }

        ScBangKeXuatVatTuHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setScBangKeXuatVatTuDtls(objReq.getChildren());
        ScBangKeXuatVatTuHdr created = hdrRepository.save(data);
        return created;
    }


    public ScBangKeXuatVatTuHdr detail(Long id) throws Exception {
        if (id == null)
            throw new Exception("Tham số không hợp lệ.");
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        return optional.get();
    }


    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (ObjectUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScBangKeXuatVatTuHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<ScBangKeXuatVatTuHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public ScBangKeXuatVatTuHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<ScBangKeXuatVatTuHdr> optional) throws Exception {
//        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
//        switch (status) {
//            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
//                optional.get().setNgayGDuyet(LocalDate.now());
//                optional.get().setNguoiGDuyet(currentUser.getUser().getId());
//                break;
//            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
//                optional.get().setNgayPDuyet(LocalDate.now());
//                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
//                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
//                break;
//            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
//                optional.get().setNgayPDuyet(LocalDate.now());
//                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
//                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(optional.get().getMaDvi(),
//                        optional.get().getQDinhDccId(),
//                        optional.get().getMaNganKho(),
//                        optional.get().getMaLoKho());
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(optional.get().getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                dataLinkDtl.setType(DcnbBangKeNhapVTHdr.TABLE_NAME);
//                break;
//            default:
//                throw new Exception("Phê duyệt không thành công");
//        }
//        optional.get().setTrangThai(statusReq.getTrangThai());
//        DcnbBangKeNhapVTHdr created = hdrRepository.save(optional.get());
//        return created;
        return null;
    }


    public void delete(Long id) throws Exception {
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScBangKeXuatVatTuHdr data = optional.get();
        List<ScBangKeXuatVatTuDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);
        hdrRepository.delete(data);
    }


    public void deleteMulti(List<Long> listMulti) throws Exception {
        List<ScBangKeXuatVatTuHdr> list = hdrRepository.findAllByIdIn(listMulti);

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(ScBangKeXuatVatTuHdr::getId).collect(Collectors.toList());
        List<ScBangKeXuatVatTuDtl> listBangKe = dtlRepository.findByHdrIdIn(listId);
        dtlRepository.deleteAll(listBangKe);
    }

    public void export(ScBangKeXuatVatTuReq objReq, HttpServletResponse response) throws Exception {

    }
}
