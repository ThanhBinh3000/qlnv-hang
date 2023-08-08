package com.tcdt.qlnvhang.service.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgRequest;
import com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu.XhXkTongHopKhXuatCuc;
import com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu.XhXkTongHopKhXuatHangDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkDsHangDtqgService extends BaseServiceImpl {
    @Autowired
    private XhXkDsHangDtqgRepository xhXkDsHangDtqgRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private QlnvDmVattuRepository qlnvDmVattuRepository;
    @Autowired
    private XhXkDsHangDtqgDtlRepository xhXkDsHangDtqgDtlRepository;

    public Page<XhXkDsHangDtqgHdr> searchPage(CustomUserDetails currentUser, XhXkDsHangDtqgRequest req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhXkDsHangDtqgHdr> search = xhXkDsHangDtqgRepository.searchPage(req, pageable);
        search.getContent().forEach(s -> {
            s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
        });
        return search;
    }

    @Transactional
    public XhXkDsHangDtqgHdr save(CustomUserDetails currentUser, XhXkDsHangDtqgRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        XhXkDsHangDtqgHdr data = new XhXkDsHangDtqgHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DUTHAO);
        XhXkDsHangDtqgHdr created = xhXkDsHangDtqgRepository.save(data);
        //Save detail
        this.saveDetailDs(created.getId(), created.getLoai());
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkDsHangDtqgHdr.TABLE_NAME);
        return detail(created.getId());
    }

    @Transactional()
    public XhXkDsHangDtqgHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkDsHangDtqgHdr> optional = xhXkDsHangDtqgRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkDsHangDtqgHdr model = optional.get();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkDsHangDtqgHdr.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkDsHangDtqgHdr> optional = xhXkDsHangDtqgRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkDsHangDtqgHdr data = optional.get();
        xhXkDsHangDtqgRepository.delete(data);
    }


    public XhXkDsHangDtqgHdr pheDuyet(CustomUserDetails currentUser, StatusReq req) throws Exception {
        Optional<XhXkDsHangDtqgHdr> dx = xhXkDsHangDtqgRepository.findById(req.getId());
        if (!dx.isPresent()) {
            throw new Exception("Không tồn tại bản ghi");
        }
        XhXkDsHangDtqgHdr xhXkDsHangDtqgHdr = dx.get();
        String status = xhXkDsHangDtqgHdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.DU_THAO + Contains.CHO_DUYET_TP:
            case Contains.TU_CHOI_TP + Contains.CHO_DUYET_TP:
            case Contains.TU_CHOI_LDC + Contains.CHO_DUYET_TP:
                break;
            case Contains.CHO_DUYET_TP + Contains.TU_CHOI_TP:
            case Contains.CHO_DUYET_LDC + Contains.CHO_DUYET_TP:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
            case Contains.CHO_DUYET_BTC + Contains.TU_CHOI_BTC:
            case Contains.CHODUYET_LDTC + Contains.TUCHOI_LDTC:
                xhXkDsHangDtqgHdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHO_DUYET_LDC + Contains.DA_DUYET_LDC:
            case Contains.CHO_DUYET_TP + Contains.CHO_DUYET_LDC:
            case Contains.CHODUYET_LDV + Contains.CHODUYET_LDTC:
            case Contains.CHODUYET_LDTC + Contains.CHO_DUYET_BTC:
            case Contains.CHO_DUYET_BTC + Contains.DA_DUYET_BTC:
            case Contains.DU_THAO + Contains.CHODUYET_LDV:
                xhXkDsHangDtqgHdr.setNguoiDuyetId(currentUser.getUser().getId());
                xhXkDsHangDtqgHdr.setNgayDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công.");
        }
        xhXkDsHangDtqgHdr.setTrangThai(req.getTrangThai());
        XhXkDsHangDtqgHdr model = xhXkDsHangDtqgRepository.save(xhXkDsHangDtqgHdr);
        return detail(model.getId());
    }

    public List<XhXkDsHangDtqgDtl> saveDetailDs(Long idHdr, Integer loaiDs) throws Exception {
        //Xóa và save lại
        xhXkDsHangDtqgDtlRepository.deleteAllByIdHdr(idHdr);
        List<XhXkDsHangDtqgDtl> list = new ArrayList<>();
        List<QlnvDmVattu> qlnvDmVattus = qlnvDmVattuRepository.listHangDtqg(loaiDs);
        if (!qlnvDmVattus.isEmpty()) {
            qlnvDmVattus.forEach(item -> {
                XhXkDsHangDtqgDtl model = new XhXkDsHangDtqgDtl();
                BeanUtils.copyProperties(item, model);
                model.setIdHdr(idHdr);
                list.add(model);
            });
        }
        return xhXkDsHangDtqgDtlRepository.saveAll(list);
    }


}
