package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlToChucRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlToChuc;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlToChucHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucHdr;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhTlToChucService extends BaseServiceImpl {

    @Autowired
    private XhTlToChucRepository xhTlToChucRepository;

    @Autowired
    private XhTlQuyetDinhDtlRepository xhTlQuyetDinhDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhTlToChucHdr> searchPage(CustomUserDetails currentUser, SearchXhTlToChuc req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 4));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlToChucHdr> search = xhTlToChucRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(f -> {
            f.getToChucDtl().forEach(s ->{
                s.setMapDmucDvi(mapDmucDvi);
                s.setMapVthh(mapVthh);
            });
            f.setTenDvi(mapDmucDvi.containsKey(f.getMaDvi()) ? mapDmucDvi.get(f.getMaDvi()) : null);
            f.setTrangThai(f.getTrangThai());
        });
        return search;
    }

    @Transactional
    public XhTlToChucHdr create(CustomUserDetails currentUser, XhTlToChucHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");

        XhTlToChucHdr data = new XhTlToChucHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setId(Long.valueOf(req.getMaThongBao().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        data.getToChucDtl().forEach(f -> {
            f.setToChucHdr(data);
        });
        data.getToChucNlq().forEach(f -> {
            f.setToChucHdr(data);
        });

        Optional<XhTlQuyetDinhDtl> byId = xhTlQuyetDinhDtlRepository.findById(data.getIdQdTlDtl());
        if (byId.isPresent()) {
            byId.get().setTrangThaiThucHien(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            xhTlQuyetDinhDtlRepository.save(byId.get());
        }

        XhTlToChucHdr created = xhTlToChucRepository.save(data);

        if (!DataUtils.isNullOrEmpty(req.getFileCanCu())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileCanCu(), created.getId(), XhTlToChucHdr.TABLE_NAME);
        }

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhTlToChucHdr.TABLE_NAME + "_DINH_KEM");
        }

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKemDaKy())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemDaKy(), created.getId(), XhTlToChucHdr.TABLE_NAME + "DA_KY");
        }
        return created;
    }

    @Transactional
    public XhTlToChucHdr update(CustomUserDetails currentUser, XhTlToChucHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");

        Optional<XhTlToChucHdr> optional = xhTlToChucRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");

        XhTlToChucHdr data = optional.get();
        req.getToChucDtl().forEach(f -> {
            f.setToChucHdr(null);
        });
        req.getToChucNlq().forEach(f -> {
            f.setToChucHdr(null);
        });

        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.getToChucDtl().forEach(f -> {
            f.setToChucHdr(data);
            if (req.getKetQua().equals(0)){
                f.setGiaKhoiDiem(null);
                f.setDonGiaCaoNhat(null);
                f.setSoLanTraGia(null);
                f.setToChucCaNhan(null);
            }
        });
        if (req.getKetQua().equals(1)){
            data.getToChucNlq().forEach(f -> {
                f.setToChucHdr(data);
            });
        }

        XhTlToChucHdr created = xhTlToChucRepository.save(data);

        fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhTlToChucHdr.TABLE_NAME));

        fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhTlToChucHdr.TABLE_NAME + "_DINH_KEM"));

        fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhTlToChucHdr.TABLE_NAME + "DA_KY"));

        if (!DataUtils.isNullOrEmpty(req.getFileCanCu())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileCanCu(), created.getId(), XhTlToChucHdr.TABLE_NAME);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhTlToChucHdr.TABLE_NAME + "_DINH_KEM");
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKemDaKy())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemDaKy(), created.getId(), XhTlToChucHdr.TABLE_NAME + "DA_KY");
        }

        return created;
    }

    public List<XhTlToChucHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhTlToChucHdr> listById = xhTlToChucRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(listById)) throw new Exception("Không tìm thấy dữ liệu.");

        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        List<XhTlToChucHdr> listAllById = xhTlToChucRepository.findAllById(ids);
        listAllById.forEach(data -> {
            data.setTenDvi(mapDmucDvi.containsKey(data.getMaDvi()) ? mapDmucDvi.get(data.getMaDvi()) : null);
            data.setTrangThai(data.getTrangThai());

            List<FileDinhKem> fileCanCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlToChucHdr.TABLE_NAME));
            data.setFileCanCu(fileCanCu);

            List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlToChucHdr.TABLE_NAME + "_DINH_KEM"));
            data.setFileDinhKem(fileDinhKem);

            List<FileDinhKem> fileDinhKemDaKy = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlToChucHdr.TABLE_NAME + "DA_KY"));
            data.setFileDinhKemDaKy(fileDinhKemDaKy);

            data.getToChucDtl().forEach(f -> {
                f.setMapDmucDvi(mapDmucDvi);
                f.setMapVthh(mapDmucVthh);
            });
        });
        return listAllById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhTlToChucHdr> optional = xhTlToChucRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");

        XhTlToChucHdr data = optional.get();

        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlToChucHdr.TABLE_NAME));
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlToChucHdr.TABLE_NAME + "_DINH_KEM"));
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlToChucHdr.TABLE_NAME + "DA_KY"));
        xhTlToChucRepository.delete(data);
    }
}
