package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBangKeNhapVTDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBangKeNhapVTHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeNhapVTReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBangKeNhapVTService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLinkDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLinkHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DcnbBangKeNhapVTServiceImpl implements DcnbBangKeNhapVTService {
    @Autowired
    DcnbBangKeNhapVTHdrRepository hdrRepository;
    @Autowired
    DcnbBangKeNhapVTDtlRepository dtlRepository;
    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;


    @Override
    public Page<DcnbBangKeNhapVTHdr> searchPage(DcnbBangKeNhapVTReq req) throws Exception {
        return null;
    }

    public Page<DcnbBangKeNhapVTHdrDTO> searchPage(CustomUserDetails currentUser, DcnbBangKeNhapVTReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBangKeNhapVTHdrDTO> searchDto = null;
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            searchDto = hdrRepository.searchPage(req, pageable);
        } else {
            req.setTypeDataLink(Contains.DIEU_CHUYEN);
            searchDto = hdrRepository.searchPageCuc(req, pageable);
        }
        return searchDto;
    }

    @Override
    public DcnbBangKeNhapVTHdr create(DcnbBangKeNhapVTReq objReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        String dvql = userInfo.getDvql();
        Optional<DcnbBangKeNhapVTHdr> optional = hdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
        if (optional.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
            throw new Exception("Số bảng kê đã tồn tại");
        }
        DcnbBangKeNhapVTHdr data = new DcnbBangKeNhapVTHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(dvql);
        data.setTenDvi(userInfo.getTenDvi());
        objReq.getDcnbbangkenhapvtdtl().forEach(e -> e.setBcnbBangKeNhapVTHdr(data));
        DcnbBangKeNhapVTHdr created = hdrRepository.save(data);
        return created;
    }

    @Override
    public DcnbBangKeNhapVTHdr update(DcnbBangKeNhapVTReq objReq) throws Exception {
        Optional<DcnbBangKeNhapVTHdr> optional = hdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<DcnbBangKeNhapVTHdr> soDxuat = hdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBangKe())) {
            if (soDxuat.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
                if (!soDxuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("số bảng kê đã tồn tại");
                }
            }
        }

        DcnbBangKeNhapVTHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setDcnbBangKeNhapVTDtl(objReq.getDcnbbangkenhapvtdtl());
        DcnbBangKeNhapVTHdr created = hdrRepository.save(data);
        return created;
    }

    @Override
    public DcnbBangKeNhapVTHdr detail(Long id) throws Exception {
        if (id == null)
            throw new Exception("Tham số không hợp lệ.");
        Optional<DcnbBangKeNhapVTHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        return optional.get();
    }

    @Override
    public DcnbBangKeNhapVTHdr approve(DcnbBangKeNhapVTReq objReq) throws Exception {
        return null;
    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBangKeNhapVTHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbBangKeNhapVTHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbBangKeNhapVTHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbBangKeNhapVTHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
                optional.get().setNgayGDuyet(LocalDate.now());
                optional.get().setNguoiGDuyet(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(optional.get().getMaDvi(),
                        optional.get().getQDinhDccId(),
                        optional.get().getMaNganKho(),
                        optional.get().getMaLoKho());
                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
                dataLinkDtl.setLinkId(optional.get().getId());
                dataLinkDtl.setHdrId(dataLink.getId());
                dataLinkDtl.setType(DcnbBangKeNhapVTHdr.TABLE_NAME);
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbBangKeNhapVTHdr created = hdrRepository.save(optional.get());
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<DcnbBangKeNhapVTHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbBangKeNhapVTHdr data = optional.get();
        List<DcnbBangKeNhapVTDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);
        hdrRepository.delete(data);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        List<DcnbBangKeNhapVTHdr> list = hdrRepository.findAllByIdIn(listMulti);

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(DcnbBangKeNhapVTHdr::getId).collect(Collectors.toList());
        List<DcnbBangKeNhapVTDtl> listBangKe = dtlRepository.findByHdrIdIn(listId);
        dtlRepository.deleteAll(listBangKe);
    }

    @Override
    public void export(DcnbBangKeNhapVTReq objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<DcnbBangKeNhapVTHdrDTO> page = searchPage(currentUser,objReq);
        List<DcnbBangKeNhapVTHdrDTO> data = page.getContent();

        String title = "Danh sách bảng kê cân hàng ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/đề xuất", "Ngày lập KH", "Ngày duyệt LĐ Chi cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBangKeNhapVTHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
