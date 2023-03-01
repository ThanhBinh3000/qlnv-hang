package com.tcdt.qlnvhang.service.xuathang.bantructiep.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrRepository;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class XhQdNvXhBttServiceImpI extends BaseServiceImpl implements XhQdNvXhBttService {

    @Autowired
    private XhQdNvXhBttHdrRepository xhQdNvXhBttHdrRepository;

    @Autowired
    private XhQdNvXhBttDtlRepository xhQdNvXhBttDtlRepository;

    @Autowired
    private XhQdNvXhBttDviRepository xhQdNvXhBttDviRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;

    @Override
    public Page<XhQdNvXhBttHdr> searchPage(XhQdNvXhBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdNvXhBttHdr> data = xhQdNvXhBttHdrRepository.searchPage(
                req,
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        data.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public XhQdNvXhBttHdr create(XhQdNvXhBttHdrReq req) throws Exception {

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        if (!StringUtils.isEmpty(req.getSoQd())){
            List<XhQdNvXhBttHdr> checkSoQd = xhQdNvXhBttHdrRepository.findBySoQd(req.getSoQd());
            if (!checkSoQd.isEmpty()){
                throw new Exception("Số quyết định" + req.getSoQd() + "đã tồn tại");
            }
        }

        XhHopDongBttHdr dataHd = new XhHopDongBttHdr();
        XhQdPdKhBttHdr dataQdPdKh = new XhQdPdKhBttHdr();
        if (req.getPhanLoai().equals("HD")){
            Optional<XhHopDongBttHdr> qOptionalHd = xhHopDongBttHdrRepository.findById(req.getIdHd());
            if (!qOptionalHd.isPresent()){
                throw new Exception("Không tìm thấy hợp đồng bán trực tiếp");
            }
            dataHd = qOptionalHd.get();
        }else {
            Optional<XhQdPdKhBttHdr> qOptionalQdPdKh = xhQdPdKhBttHdrRepository.findById(req.getIdQdPdKh());
            if (!qOptionalQdPdKh.isPresent()){
                throw new Exception("Không tìm thấy quyết định phê duyệt kế hoạch bán trực tiếp");
            }
            dataQdPdKh = qOptionalQdPdKh.get();
        }

        XhQdNvXhBttHdr  dataMap = new XhQdNvXhBttHdr();
        BeanUtils.copyProperties(req, dataMap, "id");
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setNguoiTaoId(getUser().getId());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setMaDvi(userInfo.getDvql());
        XhQdNvXhBttHdr created = xhQdNvXhBttHdrRepository.save(dataMap);
        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(req.getFileDinhKem()), created.getId(), XhQdNvXhBttHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKem);
        }

        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), dataMap.getId(), XhQdNvXhBttHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        this.saveDetail(req, dataMap.getId());
        return created;
    }

    public void saveDetail(XhQdNvXhBttHdrReq req, Long idHdr){
        xhQdNvXhBttDtlRepository.deleteAllByIdQdHdr(idHdr);
        for (XhQdNvXhBttDtlReq dtlReq : req.getChildren()){
            XhQdNvXhBttDtl dtl = new XhQdNvXhBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdQdHdr(idHdr);
            dtl.setTrangThai(Contains.CHUACAPNHAT);
            xhQdNvXhBttDtlRepository.save(dtl);
            xhQdNvXhBttDviRepository.deleteAllByIdDtl(dtl.getId());
            for (XhQdNvXhBttDviReq dviReq : dtlReq.getChildren()){
                XhQdNvXhBttDvi dvi = new XhQdNvXhBttDvi();
                BeanUtils.copyProperties(dviReq, dvi, "id");
                dvi.setId(null);
                dvi.setIdDtl(dtl.getId());
                xhQdNvXhBttDviRepository.save(dvi);
            }
        }
    }

    @Override
    public XhQdNvXhBttHdr update(XhQdNvXhBttHdrReq req) throws Exception {
        return null;
    }

    @Override
    public XhQdNvXhBttHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public XhQdNvXhBttHdr approve(XhQdNvXhBttHdrReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhQdNvXhBttHdrReq req, HttpServletResponse response) throws Exception {

    }
}
