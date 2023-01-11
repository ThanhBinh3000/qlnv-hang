package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua.XhKqBdgHdrRepository;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class XhKqBdgHdrServiceImpl extends BaseServiceImpl implements XhKqBdgHdrService {

    @Autowired
    private XhKqBdgHdrRepository xhKqBdgHdrRepository;

    @Override
    public Page<XhKqBdgHdr> searchPage(XhKqBdgHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhKqBdgHdr> page = xhKqBdgHdrRepository.search(req,pageable);
        return page;
    }

    @Override
    public List<XhKqBdgHdr> searchAll(XhKqBdgHdrReq req) {
        return null;
    }

    @Override
    public XhKqBdgHdr create(XhKqBdgHdrReq req) throws Exception {
        XhKqBdgHdr data = new XhKqBdgHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNam(LocalDate.now().getYear());
        data.setNguoiTaoId(getUser().getId());
        data.setNgayTao(new Date());
        data.setMaDvi(getUser().getDvql());
        xhKqBdgHdrRepository.save(data);
        return data;
    }

    @Override
    public XhKqBdgHdr update(XhKqBdgHdrReq req) throws Exception {
        if(ObjectUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhKqBdgHdr> byId = xhKqBdgHdrRepository.findById(req.getId());
        if (!byId.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhKqBdgHdr data = byId.get();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(new Date());
        data.setNguoiSuaId(getUser().getId());
        xhKqBdgHdrRepository.save(data);
        return data;
    }

    @Override
    public XhKqBdgHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhKqBdgHdr> byId = xhKqBdgHdrRepository.findById(id);
        if (!byId.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhKqBdgHdr data = byId.get();
        return data;
    }

    @Override
    public XhKqBdgHdr approve(XhKqBdgHdrReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhKqBdgHdr> byId = xhKqBdgHdrRepository.findById(id);
        if (!byId.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        xhKqBdgHdrRepository.delete(byId.get());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (ObjectUtils.isEmpty(listMulti)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        listMulti.forEach(item -> {
            try {
                this.delete(item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void export(XhKqBdgHdrReq req, HttpServletResponse response) throws Exception {

    }

}

