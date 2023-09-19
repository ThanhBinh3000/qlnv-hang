package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangkecanhang;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeChCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeChCtLtReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Log4j2
public class NhBangKeCanHangServiceImpl extends BaseServiceImpl implements NhBangKeCanHangService {

    @Autowired
    private NhBangKeCanHangRepository nhBangKeCanHangRepository;

    @Autowired
    private NhBangKeChCtRepository nhBangKeChCtRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private NhPhieuNhapKhoRepository nhPhieuNhapKhoRepository;

    @Autowired
    private NhPhieuNhapKhoCtRepository nhPhieuNhapKhoCtRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<NhBangKeCanHang> searchPage(QlBangKeCanHangLtReq req) {
        return null;
    }

    @Override
    @Transactional
    public NhBangKeCanHang create(QlBangKeCanHangLtReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");

        }

        NhBangKeCanHang item = new NhBangKeCanHang();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.valueOf(item.getSoBangKe().split("/")[0]));
        nhBangKeCanHangRepository.save(item);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), "NH_BANG_KE_CAN_HANG");
        }
        this.saveCtiet(item.getId(),req);

        return item;
    }

    @Transactional
    void saveCtiet(Long idHdr,QlBangKeCanHangLtReq req){
        nhBangKeChCtRepository.deleteByIdBangKeCanHangHdr(idHdr);
        for(QlBangKeChCtLtReq objCtiet : req.getChiTiets()){
            NhBangKeCanHangCt ctiet = new NhBangKeCanHangCt();
            BeanUtils.copyProperties(objCtiet,ctiet,"id");
            ctiet.setIdBangKeCanHangHdr(idHdr);
            nhBangKeChCtRepository.save(ctiet);
        }
    }

    @Override
    public NhBangKeCanHang update(QlBangKeCanHangLtReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhBangKeCanHang> optional = nhBangKeCanHangRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bảng kê không tồn tại.");
        }

        NhBangKeCanHang item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        nhBangKeCanHangRepository.save(item);
        fileDinhKemService.delete(item.getId(), Lists.newArrayList("NH_BANG_KE_CAN_HANG"));
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), "NH_BANG_KE_CAN_HANG");
        }
        this.saveCtiet(item.getId(),req);
        return item;
    }

    @Override
    public NhBangKeCanHang detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhBangKeCanHang> optional = nhBangKeCanHangRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        NhBangKeCanHang item = optional.get();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi("", "", "01");

        item.setChiTiets(nhBangKeChCtRepository.findAllByIdBangKeCanHangHdr(item.getId()));
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        item.setTenNguoiTao(ObjectUtils.isEmpty(item.getNguoiTaoId()) ? "" : userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
        item.setTenNguoiPduyet(ObjectUtils.isEmpty(item.getNguoiPduyetId()) ? "" :userInfoRepository.findById(item.getNguoiPduyetId()).get().getFullName());
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(item.getId(), Collections.singletonList("NH_BANG_KE_CAN_HANG"));
        item.setListFileDinhKem(fileDinhKem);
        return item;
    }

    @Override
    public NhBangKeCanHang approve(QlBangKeCanHangLtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<NhBangKeCanHang> optional = nhBangKeCanHangRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        NhBangKeCanHang phieu = optional.get();

        String status = req.getTrangThai() + phieu.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                phieu.setNguoiGuiDuyetId(userInfo.getId());
                phieu.setNgayGuiDuyet(new Date());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                phieu.setNguoiPduyetId(userInfo.getId());
                phieu.setNgayPduyet(new Date());
                phieu.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                phieu.setNguoiPduyetId(userInfo.getId());
                phieu.setNgayPduyet(new Date());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        phieu.setTrangThai(req.getTrangThai());
        nhBangKeCanHangRepository.save(phieu);
        return phieu;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public void export(QlBangKeCanHangLtReq req, HttpServletResponse response) throws Exception {
//        return false;
    }

    @Override
    public List<NhBangKeCanHang> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh) {
        return setDetailList(nhBangKeCanHangRepository.findAllByIdQdGiaoNvNh(idQdGiaoNvNh));

    }

    @Override
    public List<NhBangKeCanHang> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh) {
        return setDetailList(nhBangKeCanHangRepository.findByIdDdiemGiaoNvNh(idDdiemGiaoNvNh));
    }

    List<NhBangKeCanHang> setDetailList(List<NhBangKeCanHang> list){
        list.forEach( item -> {
//            item.setPhieuNhapKho(StringUtils.isEmpty(item.getPhieuNhapKho()) ? null : nhPhieuNhapKhoRepository.getOne(Long.valueOf(item.getSoPhieuNhapKho().split("/")[0])));
        });
        return list;
    }

}
