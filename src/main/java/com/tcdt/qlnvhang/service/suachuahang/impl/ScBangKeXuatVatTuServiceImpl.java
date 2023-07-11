package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.response.suachua.ScBangKeXuatVtDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScBangKeXuatVatTuService;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhScService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.util.Contains;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScBangKeXuatVatTuServiceImpl extends BaseServiceImpl implements ScBangKeXuatVatTuService {
    @Autowired
    private ScBangKeXuatVatTuHdrRepository hdrRepository;

    @Autowired
    private ScBangKeXuatVatTuDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private ScQuyetDinhXuatHangRepository scQuyetDinhXuatHangRepository;

    @Autowired
    private ScQuyetDinhScService scQuyetDinhScService;

    @Autowired
    private ScPhieuXuatKhoHdrRepository scPhieuXuatKhoHdrRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Page<ScBangKeXuatVatTuHdr> searchPage(ScBangKeXuatVatTuReq req) throws Exception {
        return null;
    }

    @Override
    public ScBangKeXuatVatTuHdr create(ScBangKeXuatVatTuReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        String dvql = userInfo.getDvql();
        ScBangKeXuatVatTuHdr data = new ScBangKeXuatVatTuHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(dvql);
        data.setIdThuKho(userInfo.getId());
        data.setId(Long.parseLong(req.getSoBangKe().split("/")[0]));
        ScBangKeXuatVatTuHdr created = hdrRepository.save(data);
        List<ScBangKeXuatVatTuDtl> scBangKeXuatVatTuDtls = saveDtl(req, created.getId());
        created.setChildren(scBangKeXuatVatTuDtls);
        this.updatePhieuNhapKho(data,data.getIdPhieuXuatKho(),null);
        return created;
    }

    private void updatePhieuNhapKho(ScBangKeXuatVatTuHdr data,Long idPhieuNhapKho,Long idPhieuNhapKhoOld){
        if(!Objects.isNull(idPhieuNhapKhoOld) && !idPhieuNhapKho.equals(idPhieuNhapKhoOld)){
            Optional<ScPhieuXuatKhoHdr> byId = scPhieuXuatKhoHdrRepository.findById(idPhieuNhapKhoOld);
            if(byId.isPresent()){
                byId.get().setIdBangKeCanHang(null);
                byId.get().setSoBangKeCanHang(null);
                scPhieuXuatKhoHdrRepository.save(byId.get());
            }
        }else{
            Optional<ScPhieuXuatKhoHdr> byId = scPhieuXuatKhoHdrRepository.findById(idPhieuNhapKho);
            if(byId.isPresent()){
                byId.get().setIdBangKeCanHang(data.getId());
                byId.get().setSoBangKeCanHang(data.getSoBangKe());
                scPhieuXuatKhoHdrRepository.save(byId.get());
            }
        }
    }

    public List<ScBangKeXuatVatTuDtl> saveDtl(ScBangKeXuatVatTuReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }



    @Override
    public ScBangKeXuatVatTuHdr update(ScBangKeXuatVatTuReq req) throws Exception {
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        ScBangKeXuatVatTuHdr data = optional.get();
        this.updatePhieuNhapKho(data,req.getIdPhieuXuatKho(),data.getIdPhieuXuatKho());
        BeanUtils.copyProperties(req, data);
        ScBangKeXuatVatTuHdr created = hdrRepository.save(data);
        List<ScBangKeXuatVatTuDtl> scBangKeXuatVatTuDtls = saveDtl(req, created.getId());
        created.setChildren(scBangKeXuatVatTuDtls);
        return created;
    }

    @Override
    public ScBangKeXuatVatTuHdr detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScBangKeXuatVatTuHdr data = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        data.setChildren(dtlRepository.findByIdHdr(id));
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        if(!Objects.isNull(data.getIdThuKho())){
            data.setTenThuKho(userInfoRepository.findById(data.getIdThuKho()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdLanhDaoCc())){
            data.setTenLanhDaoCc(userInfoRepository.findById(data.getIdLanhDaoCc()).get().getFullName());
        }
        return data;
    }

    @Override
    public ScBangKeXuatVatTuHdr approve(ScBangKeXuatVatTuReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScBangKeXuatVatTuHdr hdr = optional.get();
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_LDCC + Contains.DUTHAO:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                hdr.setIdLanhDaoCc(userInfo.getId());
                break;
            // Arena từ chối
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        ScBangKeXuatVatTuHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        hdrRepository.delete(optional.get());
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
        Optional<ScPhieuXuatKhoHdr> byId = scPhieuXuatKhoHdrRepository.findById(optional.get().getIdPhieuXuatKho());
        if(byId.isPresent()){
            byId.get().setIdBangKeCanHang(null);
            byId.get().setSoBangKeCanHang(null);
            scPhieuXuatKhoHdrRepository.save(byId.get());
        }
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScBangKeXuatVatTuReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public Page<ScQuyetDinhXuatHang> searchBangKeCanHang(ScBangKeXuatVatTuReq req) {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        ScQuyetDinhXuatHangReq reqQd = new ScQuyetDinhXuatHangReq();
        reqQd.setNam(req.getNam());
        reqQd.setSoQd(req.getSoQdXh());
        Page<ScQuyetDinhXuatHang> search = scQuyetDinhXuatHangRepository.searchPageViewFromPhieuXuatKho(reqQd, pageable);
        search.getContent().forEach(item -> {
            try {
                List<ScDanhSachHdr> scDanhSachHdrList = new ArrayList<>();
                ScQuyetDinhSc scQuyetDinhSc = scQuyetDinhScService.detail(item.getIdQdSc());
                // lấy Danh sách sửa chữa hdr
                scQuyetDinhSc.getScTrinhThamDinhHdr().getChildren().forEach(( dsHdr)->{
                    ScDanhSachHdr scDanhSachHdr = dsHdr.getScDanhSachHdr();
                    req.setIdScDanhSachHdr(scDanhSachHdr.getId());
                    scDanhSachHdr.setScBangKeXuatVatTuList(hdrRepository.searchList(req));
                    scDanhSachHdrList.add(scDanhSachHdr);
                });
                item.setScQuyetDinhSc(scQuyetDinhScService.detail(item.getIdQdSc()));
                item.setScDanhSachHdrList(scDanhSachHdrList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }
}
