package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoCtReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlToChuc;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlToChucHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class XhTlToChucService extends BaseServiceImpl {

    @Autowired
    private XhTlToChucHdrRepository hdrRepository;

    @Autowired
    private XhTlToChucDtlRepository dtlRepository;

    @Autowired
    private XhTlToChucNlqRepository nlqRepository;

    @Autowired
    private XhTlQuyetDinhHdrRepository xhTlQuyetDinhHdrRepository;

    @Autowired
    private XhTlDanhSachRepository xhTlDanhSachRepository;

    @Autowired
    private XhTlDanhSachService xhTlDanhSachService;

    public Page<XhTlToChucHdr> searchPage(CustomUserDetails currentUser, SearchXhTlToChuc req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 4));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlToChucHdr> search = hdrRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(f -> {
            f.setMapDmucDvi(mapDmucDvi);
            f.setTrangThai(f.getTrangThai());
        });
        return search;
    }

    public List<XhTlToChucHdr> searchAll( SearchXhTlToChuc req) throws Exception {
        List<XhTlToChucHdr> search = hdrRepository.searchAll(req);
        search.forEach( item -> {
            List<XhTlToChucDtl> allByIdHdr = dtlRepository.findAllByIdHdr(item.getId());
            List<XhTlToChucDtl> collect = allByIdHdr.stream().filter(x -> x.getMaDviTsan() != null).collect(Collectors.toList());
            item.setSoDviTs(collect.size());
            item.setTongDviTs(allByIdHdr.size());
        });
        return search;
    }

    public List<XhTlToChucHdr> dsTaoQuyetDinhPdKq( SearchXhTlToChuc req) throws Exception {
        List<XhTlToChucHdr> search = hdrRepository.dsTaoQdPdKq(req);
        return search;
    }

    @Transactional
    public XhTlToChucHdr create(CustomUserDetails currentUser, XhTlToChucHdrReq req) throws Exception {
        if (currentUser == null){
            throw new Exception("Bad request.");
        }

        XhTlToChucHdr data = new XhTlToChucHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDvql());
        data.setId(Long.valueOf(req.getMaThongBao().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        Optional<XhTlQuyetDinhHdr> byId = xhTlQuyetDinhHdrRepository.findById(data.getIdQdTl());
        if (byId.isPresent()) {
            byId.get().setTrangThaiDg(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            xhTlQuyetDinhHdrRepository.save(byId.get());
        }
        XhTlToChucHdr created = hdrRepository.save(data);
        this.saveDetail(req,created.getId());
        return created;
    }

    private void saveDetail(XhTlToChucHdrReq req,Long idHdr) throws Exception {
        // Save địa điểm
        dtlRepository.deleteAllByIdHdr(idHdr);
        for(XhTlToChucDtl dtl : req.getChildren()){
            Optional<XhTlDanhSachHdr> byId = xhTlDanhSachRepository.findById(dtl.getIdDsHdr());
            if(byId.isPresent()){
                // Save dtl
                XhTlToChucDtl ct = new XhTlToChucDtl();
                BeanUtils.copyProperties(dtl, ct,"id");
                ct.setId(null);
                ct.setIdHdr(idHdr);
                ct.setIdDsHdr(dtl.getIdDsHdr());
                dtlRepository.save(ct);
                // Update lại các trường vào DS gốc
                XhTlDanhSachHdr ds = byId.get();
                ds.setMaDviTsan(dtl.getMaDviTsan());
                ds.setSoLanTraGia(dtl.getSoLanTraGia());
                ds.setDonGiaCaoNhat(dtl.getDonGiaCaoNhat());
                ds.setToChucCaNhan(dtl.getToChucCaNhan());
                // Nêu dấu giá thành công
                if(req.getKetQua() == 1){
                    // Và có tổ chức cá nhân thì ms set kết quả
                    if(!StringUtils.isEmpty(ds.getToChucCaNhan())){
                        ds.setKetQuaDauGia(req.getKetQua());
                    }
                }else{
                    // Nếu dấu giá không thành công thì có mã đơn vị tài sản thì ms set kết quả
                    if(!StringUtils.isEmpty(ds.getMaDviTsan())){
                        ds.setKetQuaDauGia(req.getKetQua());
                    }
                }

                xhTlDanhSachRepository.save(ds);

            }else{
                throw new Exception("Không tìm thấy danh sách xuất thanh lý");
            }
        }
        // Save người đấu giá
        nlqRepository.deleteAllByIdHdr(idHdr);
        for(XhTlToChucNlq nlq : req.getChildrenNlq()){
            XhTlToChucNlq ct = new XhTlToChucNlq();
            BeanUtils.copyProperties(nlq, ct,"id");
            ct.setId(null);
            ct.setIdHdr(idHdr);
            nlqRepository.save(ct);
        }
    }

    @Transactional
    public XhTlToChucHdr update(CustomUserDetails currentUser, XhTlToChucHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhTlToChucHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        XhTlToChucHdr data = optional.get();
//        req.getToChucDtl().forEach(f -> {
//            f.setToChucHdr(null);
//        });
//        req.getToChucNlq().forEach(f -> {
//            f.setToChucHdr(null);
//        });
        BeanUtils.copyProperties(req, data, "id", "maDvi");
//        data.getToChucDtl().forEach(f -> {
//            f.setToChucHdr(data);
//            if (req.getKetQua().equals(0)) {
//                f.setGiaKhoiDiem(null);
//                f.setDonGiaCaoNhat(null);
//                f.setSoLanTraGia(null);
//                f.setToChucCaNhan(null);
//            }
//        });

        XhTlToChucHdr updated = hdrRepository.save(data);
        this.saveDetail(req,updated.getId());
        return updated;
    }

    public XhTlToChucHdr detail(Long id) throws Exception {
        if (Objects.isNull(id)){
            throw new Exception("Tham số không hợp lệ.");
        }
        Optional<XhTlToChucHdr> optById = hdrRepository.findById(id);
        if(!optById.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu.");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi("02", null, "01");
        XhTlToChucHdr data = optById.get();
        data.setMapDmucDvi(mapDmucDvi);
        data.setTrangThai(data.getTrangThai());
        data.setChildrenNlq(nlqRepository.findAllByIdHdr(data.getId()));
        List<XhTlToChucDtl> allByIdHdr = dtlRepository.findAllByIdHdr(data.getId());
        allByIdHdr.forEach(item -> {
            try {
                item.setXhTlDanhSachHdr(xhTlDanhSachService.detail(item.getIdDsHdr()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        data.setChildren(dtlRepository.findAllByIdHdr(data.getId()));
        return data;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhTlToChucHdr> optional = hdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        hdrRepository.delete(optional.get());
        nlqRepository.deleteAllByIdHdr(optional.get().getId());
        List<XhTlToChucDtl> allByIdHdr = dtlRepository.findAllByIdHdr(idSearchReq.getId());
        allByIdHdr.forEach( dtl -> {
            Optional<XhTlDanhSachHdr> byId = xhTlDanhSachRepository.findById(dtl.getIdDsHdr());
            if(byId.isPresent()) {
                // Update lại các trường vào DS gốc
                XhTlDanhSachHdr ds = byId.get();
                ds.setMaDviTsan(null);
                ds.setSoLanTraGia(null);
                ds.setDonGiaCaoNhat(null);
                ds.setToChucCaNhan(null);
                ds.setKetQuaDauGia(null);
                xhTlDanhSachRepository.save(ds);
            }
        });
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
    }

}
