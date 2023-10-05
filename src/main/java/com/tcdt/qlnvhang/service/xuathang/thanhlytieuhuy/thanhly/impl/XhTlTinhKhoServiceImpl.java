package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdrReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlTinhKhoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachService;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlTinhKhoService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class XhTlTinhKhoServiceImpl extends BaseServiceImpl implements XhTlTinhKhoService {

    @Autowired
    private XhTlTinhKhoHdrRepository hdrRepository;

    @Autowired
    private XhTlTinhKhoDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private XhTlQdGiaoNvHdrRepository xhTlQdGiaoNvHdrRepository;

    @Autowired
    private XhTlQdGiaoNvDtlRepository xhTlQdGiaoNvDtlRepository;

    @Autowired
    private XhTlDanhSachService xhTlDanhSachService;

    @Autowired
    private XhTlKtraClHdrRepository xhTlKtraClHdrRepository;


    @Override
    public Page<XhTlTinhKhoHdr> searchPage(XhTlTinhKhoReq req) throws Exception {
        return null;
    }

    @Override
    public XhTlTinhKhoHdr create(XhTlTinhKhoReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        XhTlTinhKhoHdr data = new XhTlTinhKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setId(Long.parseLong(req.getSoBbTinhKho().split("/")[0]));
        data.setIdThuKho(currentUser.getId());
        XhTlTinhKhoHdr created = hdrRepository.save(data);
        saveFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhTlTinhKhoHdr.TABLE_NAME);
        saveDtl(req, data.getId());
        return created;
    }

    public void saveDtl(XhTlTinhKhoReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
    }

    @Override
    public XhTlTinhKhoHdr update(XhTlTinhKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<XhTlTinhKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTlTinhKhoHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        fileDinhKemService.delete(data.getId(), Collections.singleton(XhTlTinhKhoHdr.TABLE_NAME));
        saveFileDinhKem(req.getFileDinhKemReq(), data.getId(), XhTlTinhKhoHdr.TABLE_NAME);
        saveDtl(req, data.getId());
        return data;
    }

    @Override
    public XhTlTinhKhoHdr detail(Long id) throws Exception {
        Optional<XhTlTinhKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTlTinhKhoHdr data = optional.get();
        data.setFileDinhKem(fileDinhKemService.search(id, Collections.singleton(XhTlTinhKhoHdr.TABLE_NAME)));
        data.setChildren(dtlRepository.findAllByIdHdr(id));
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        //set label
        data.setMapDmucDvi(mapDmucDvi);
        data.setMapVthh(mapVthh);
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
//        if(!Objects.isNull(data.getIdThuKho())){
//            data.setTenThuKho(userInfoRepository.findById(data.getIdThuKho()).get().getFullName());
//        }
//        if(!Objects.isNull(data.getIdLanhDaoCc())){
//            data.setTenLanhDaoCc(userInfoRepository.findById(data.getIdLanhDaoCc()).get().getFullName());
//        }
        return data;
    }

    @Override
    public XhTlTinhKhoHdr approve(XhTlTinhKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhTlTinhKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhTlTinhKhoHdr hdr = optional.get();

        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_KTVBQ + Contains.DUTHAO:
            case Contains.TUCHOI_KT + Contains.DUTHAO:
            case Contains.TUCHOI_LDCC + Contains.DUTHAO:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_KTVBQ:
            case Contains.CHODUYET_KTVBQ + Contains.CHODUYET_KT:
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
//                hdr.setIdLanhDaoCc(userInfo.getId());
                break;
            // Arena từ chối
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
            case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        XhTlTinhKhoHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<XhTlTinhKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        hdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME));
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME+"_CC"));
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhTlTinhKhoReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public List<XhTlTinhKhoHdr> searchDsTaoBbHaoDoi(XhTlTinhKhoReq req) throws Exception {
        return null;
    }

    @Override
    public Page<XhTlQdGiaoNvHdr> searchXhTlTinhKho(XhTlTinhKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        XhTlQdGiaoNvHdrReq reqQd = new XhTlQdGiaoNvHdrReq();
        reqQd.setNam(req.getNam());
        reqQd.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        reqQd.setPhanLoai(req.getPhanLoai());
        if(userInfo.getCapDvi().equals(Contains.CAP_CUC)){
            reqQd.setMaDviSr(userInfo.getDvql());
            req.setMaDviSr(userInfo.getDvql());
        }
        if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            reqQd.setMaDviSr(userInfo.getDvql().substring(0, 6));
            req.setMaDviSr(userInfo.getDvql().substring(0, 6));
        }
        Page<XhTlQdGiaoNvHdr> search = xhTlQdGiaoNvHdrRepository.searchPageViewFromAnother(reqQd, pageable);
        search.getContent().forEach(item -> {
            try {
                // Lấy toàn bộ địa điểm kho trong dtl
                List<XhTlQdGiaoNvDtl> byIdHdr = xhTlQdGiaoNvDtlRepository.findAllByIdHdrAndPhanLoai(item.getId(),req.getPhanLoai());
                byIdHdr.forEach( x -> {
                    try {
                        // Lấy danh sách gốc
                        XhTlDanhSachHdr dsHdr = xhTlDanhSachService.detail(x.getIdDsHdr());
                        x.setXhTlDanhSachHdr(dsHdr);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                item.setChildren(byIdHdr);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

}