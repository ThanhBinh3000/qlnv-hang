package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHaoDoiHdrReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdrReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlTinhKhoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachService;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlHaoDoiService;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhTlHaoDoiServiceImpl extends BaseServiceImpl implements XhTlHaoDoiService {

    @Autowired
    private XhTlHaoDoiHdrRepository hdrRepository;


    @Autowired
    private FileDinhKemService fileDinhKemService;



    @Override
    public Page<XhTlHaoDoiHdr> searchPage(XhTlHaoDoiHdrReq req) throws Exception {
        return null;
    }

    @Override
    public XhTlHaoDoiHdr create(XhTlHaoDoiHdrReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        XhTlHaoDoiHdr data = new XhTlHaoDoiHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setId(Long.parseLong(req.getSoBbHaoDoi().split("/")[0]));
        data.setIdThuKho(currentUser.getId());
        XhTlHaoDoiHdr created = hdrRepository.save(data);
        saveFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhTlHaoDoiHdr.TABLE_NAME);
        return created;
    }



    @Override
    public XhTlHaoDoiHdr update(XhTlHaoDoiHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<XhTlHaoDoiHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTlHaoDoiHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        fileDinhKemService.delete(data.getId(), Collections.singleton(XhTlHaoDoiHdr.TABLE_NAME));
        saveFileDinhKem(req.getFileDinhKemReq(), data.getId(), XhTlHaoDoiHdr.TABLE_NAME);
        return data;
    }

    @Override
    public XhTlHaoDoiHdr detail(Long id) throws Exception {
        Optional<XhTlHaoDoiHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTlHaoDoiHdr data = optional.get();
        data.setFileDinhKem(fileDinhKemService.search(id, Collections.singleton(XhTlHaoDoiHdr.TABLE_NAME)));
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
    public XhTlHaoDoiHdr approve(XhTlHaoDoiHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhTlHaoDoiHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhTlHaoDoiHdr hdr = optional.get();

        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_KTVBQ:
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
        XhTlHaoDoiHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<XhTlHaoDoiHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        hdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhTlHaoDoiHdr.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhTlHaoDoiHdrReq req, HttpServletResponse response) throws Exception {

    }

}