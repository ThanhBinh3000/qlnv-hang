package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.SearchXhThopDxKhBtt;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThopDxKhBttService extends BaseServiceImpl {

    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;
    @Autowired
    private XhThopDxKhBttRepository xhThopDxKhBttRepository;
    @Autowired
    private XhThopDxKhBttDtlRepository xhThopDxKhBttDtlRepository;

    public Page<XhThopDxKhBttHdr> searchPage (SearchXhThopDxKhBtt req) throws Exception{
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhThopDxKhBttHdr> data = xhThopDxKhBttRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        data.getContent().forEach(f->{
            f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    public XhThopDxKhBttHdr sumarryData(XhThopChiTieuReq objReq , HttpServletRequest req) throws Exception{
        List<XhDxKhBanTrucTiepHdr>  dxuatBtt = xhDxKhBanTrucTiepHdrRepository.listTongHop(
                objReq.getNamKh(),
                objReq.getLoaiVthh(),
                objReq.getCloaiVthh(),
                convertDateToString(objReq.getNgayDuyetTu()),
                convertDateToString(objReq.getNgayDuyetDen()));
        if (dxuatBtt.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }

        XhThopDxKhBttHdr thopHdr = new XhThopDxKhBttHdr();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi("2", null, "01");
        String tChuanCluong = "";
        String soQdcc = "";
        List<XhThopDxKhBttDtl> thopDtls = new ArrayList<>();
        for (XhDxKhBanTrucTiepHdr dxuat : dxuatBtt) {
            XhThopDxKhBttDtl thopDtl = new XhThopDxKhBttDtl();
            BeanUtils.copyProperties(dxuat,thopDtl,"id");
            thopDtl.setIdDxHdr(dxuat.getId());
            thopDtl.setTenDvi(listDanhMucDvi.get(dxuat.getMaDvi()));
            thopDtls.add(thopDtl);
            tChuanCluong = tChuanCluong.concat(dxuat.getTchuanCluong() + "");
            soQdcc = soQdcc.concat(dxuat.getSoQdCtieu() + "");
            thopDtls.add(thopDtl);
        }
        thopHdr.setTchuanCluong(tChuanCluong);
        thopHdr.setSoQdCc(soQdcc);
        thopHdr.setChildren(thopDtls);
        return thopHdr;
    }

    @Transactional()
    public XhThopDxKhBttHdr create(XhThopDxKhBttHdrReq objReq, HttpServletRequest req) throws Exception{
        if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh())) {
            throw new Exception("Loại vật tư hàng hóa không phù hợp");
        }

        XhThopDxKhBttHdr thopHdr = sumarryData(objReq,req);
        thopHdr.setId(objReq.getIdTh());
        thopHdr.setNgayTao(new Date());
        thopHdr.setNguoiTaoId(getUser().getId());
        thopHdr.setNgayDuyetTu(objReq.getNgayDuyetTu());
        thopHdr.setNgayDuyetDen(objReq.getNgayDuyetDen());
        thopHdr.setLoaiVthh(objReq.getLoaiVthh());
        thopHdr.setCloaiVthh(objReq.getCloaiVthh());
        thopHdr.setTrangThai(Contains.CHUATAO_QD);
        thopHdr.setNgayThop(new Date());
        thopHdr.setNamKh(objReq.getNamKh());
        thopHdr.setMaDvi(objReq.getMaDvi());
        thopHdr.setNoiDungThop(objReq.getNoiDungThop());
        xhThopDxKhBttRepository.save(thopHdr);
        for (XhThopDxKhBttDtl dtl : thopHdr.getChildren()){
            dtl.setIdThopHdr(thopHdr.getId());
            xhThopDxKhBttDtlRepository.save(dtl);
        }
        if (thopHdr.getId() > 0 && thopHdr.getChildren().size() > 0) {
            List<String> soDxuatList = thopHdr.getChildren().stream().map(XhThopDxKhBttDtl::getSoDxuat)
                    .collect(Collectors.toList());
            xhDxKhBanTrucTiepHdrRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP,thopHdr.getId());
        }
        return thopHdr;
    }

    @Transactional()
    public XhThopDxKhBttHdr update (XhThopDxKhBttHdrReq objReq) throws Exception{
        if (StringUtils.isEmpty(objReq.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }
        Optional<XhThopDxKhBttHdr> qOptional = xhThopDxKhBttRepository.findById(objReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh())) {
            throw new Exception("Loại vật tư hàng hóa không phù hợp");
        }
        XhThopDxKhBttHdr dataDTB = qOptional.get();
        XhThopDxKhBttHdr dataMap = ObjectMapperUtils.map(objReq, XhThopDxKhBttHdr.class);
        updateObjectToObject(dataDTB, dataMap);
        xhThopDxKhBttRepository.save(dataDTB);
        return dataDTB;
    }


    public XhThopDxKhBttHdr detail(String ids) throws Exception {
        if (StringUtils.isEmpty(ids))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        Optional<XhThopDxKhBttHdr> qOptional = xhThopDxKhBttRepository.findById(Long.parseLong(ids));
        if (!qOptional.isPresent())
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        XhThopDxKhBttHdr hdrThop = qOptional.get();
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDmucDvi = getListDanhMucDvi(Contains.CAP_CUC,null,"01");
        hdrThop.setTenLoaiVthh(hashMapVthh.get(hdrThop.getLoaiVthh()));
        hdrThop.setTenCloaiVthh(hashMapVthh.get(hdrThop.getCloaiVthh()));
        List<XhThopDxKhBttDtl> listTh = xhThopDxKhBttDtlRepository.findByIdThopHdr(hdrThop.getId());
        listTh.forEach(f -> {
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDmucDvi.get(f.getMaDvi()));
        });
        hdrThop.setChildren(listTh);
        return hdrThop;
    }


    public void delete(IdSearchReq idSearchReq) throws Exception{
        if (StringUtils.isEmpty(idSearchReq.getId())){
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu ");
        }
        Optional<XhThopDxKhBttHdr> qOptional = xhThopDxKhBttRepository.findById(idSearchReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        XhThopDxKhBttHdr hdr = qOptional.get();
        List<XhThopDxKhBttDtl> listDtl = xhThopDxKhBttDtlRepository.findByIdThopHdr(hdr.getId());
        if (!CollectionUtils.isEmpty(listDtl)){
            List<Long> idDxList = listDtl.stream().map(XhThopDxKhBttDtl::getIdDxHdr).collect(Collectors.toList());
            List<XhDxKhBanTrucTiepHdr> listDxHdr = xhDxKhBanTrucTiepHdrRepository.findByIdIn(idDxList);
            if (!CollectionUtils.isEmpty(listDxHdr)) {
                listDxHdr.stream().map(item -> {
                    item.setTrangThaiTh(Contains.CHUATONGHOP);
                    item.setIdThop(null);
                    return item;
                }).collect(Collectors.toList());
            }
            xhDxKhBanTrucTiepHdrRepository.saveAll(listDxHdr);
        }
        xhThopDxKhBttDtlRepository.deleteAll(listDtl);
        xhThopDxKhBttRepository.delete(hdr);
    }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        List<XhThopDxKhBttHdr> listThop = xhThopDxKhBttRepository.findAllByIdIn(idSearchReq.getIdList());
        for (XhThopDxKhBttHdr thopHdr : listThop) {
            List<XhThopDxKhBttDtl> listDls = xhThopDxKhBttDtlRepository.findByIdThopHdr(thopHdr.getId());
            if (!CollectionUtils.isEmpty(listDls)) {
                List<Long> idDxList = listDls.stream().map(XhThopDxKhBttDtl::getIdDxHdr).collect(Collectors.toList());
                List<XhDxKhBanTrucTiepHdr> listDxHdr = xhDxKhBanTrucTiepHdrRepository.findByIdIn(idDxList);
                if (!CollectionUtils.isEmpty(listDxHdr)) {
                    listDxHdr.stream().map(item -> {
                        item.setTrangThaiTh(Contains.CHUATONGHOP);
                        item.setIdThop(null);
                        return item;
                    }).collect(Collectors.toList());
                }
                xhDxKhBanTrucTiepHdrRepository.saveAll(listDxHdr);
            }
            xhThopDxKhBttDtlRepository.deleteAll(listDls);
        }
        xhThopDxKhBttRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }

    public void export(SearchXhThopDxKhBtt searchReq,  HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        Page<XhThopDxKhBttHdr> page = this.searchPage(searchReq);
        List<XhThopDxKhBttHdr> data = page.getContent();
        String title = "Danh sách tổng hợp kế hoạch bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp", "Năm kế hoạch", "Số QĐ phê duyệt KH bán trực tiếp", "Loại hàng hóa", "Trạng thái"};
        String filename = "Tong_hop_ke_hoach_ban_truc_tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhThopDxKhBttHdr thop = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = thop.getId();
            objs[2] = thop.getNgayThop();
            objs[3] = thop.getNoiDungThop();
            objs[4] = thop.getNamKh();
            objs[5] = thop.getSoQdPd();
            objs[6] = thop.getLoaiVthh();
            objs[7] = thop.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
