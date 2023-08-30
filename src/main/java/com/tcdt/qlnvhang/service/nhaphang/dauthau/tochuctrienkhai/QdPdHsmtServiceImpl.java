package com.tcdt.qlnvhang.service.nhaphang.dauthau.tochuctrienkhai;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtietVt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.tochuctrienkhai.QdPdHsmt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhDchinhDxKhLcntDsgthauCtietRepository;
import com.tcdt.qlnvhang.repository.HhDchinhDxKhLcntDsgthauCtietVtRepository;
import com.tcdt.qlnvhang.repository.HhDchinhDxKhLcntDsgthauRepository;
import com.tcdt.qlnvhang.repository.HhDchinhDxKhLcntHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtietRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtietVtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.tochuctrienkhai.QdPdHsmtRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.dauthauvattu.QdPdHsmtReq;
import com.tcdt.qlnvhang.request.search.QdPdHsmtSearchReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDsgthau;
import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDsgthauCtiet;
import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDsgthauCtietVt;
import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QdPdHsmtServiceImpl extends BaseServiceImpl implements QdPdHsmtService{
    @Autowired
    private QdPdHsmtRepository qdPdHsmtRepository;

    @Autowired
    private HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private HhDchinhDxKhLcntHdrRepository hhDchinhDxKhLcntHdrRepository;

    @Autowired
    private HhDchinhDxKhLcntDsgthauRepository gThauRepository;

    @Autowired
    private HhDchinhDxKhLcntDsgthauCtietRepository gThauCietRepository;

    @Autowired
    private HhDchinhDxKhLcntDsgthauCtietVtRepository gThauCietVtRepository;

    @Autowired
    private HhQdKhlcntDsgthauRepository hhQdKhlcntDsgthauRepository;

    @Autowired
    private HhQdKhlcntDsgthauCtietRepository hhQdKhlcntDsgthauCtietRepository;

    @Autowired
    private HhQdKhlcntDsgthauCtietVtRepository hhQdKhlcntDsgthauCtietVtRepository;

    @Autowired
    private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

    @Override
    public Page<QdPdHsmt> timKiem(QdPdHsmtSearchReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<QdPdHsmt> page = qdPdHsmtRepository.search(req.getNamKhoach(), req.getSoQd(), req.getSoQdPdKhlcnt(), convertFullDateToString(req.getTuNgayKy()), convertFullDateToString(req.getDenNgayKy()), req.getLoaiVthh(), req.getTrichYeu(), req.getTrangThai(), req.getMaDvi(), pageable);
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        page.getContent().forEach(f -> {
            Optional<HhQdKhlcntHdr> qdKhlcntHdr = hhQdKhlcntHdrRepository.findById(f.getIdQdPdKhlcnt());
            if (qdKhlcntHdr.isPresent()) {
                qdKhlcntHdr.get().setTenLoaiVthh(StringUtils.isEmpty(qdKhlcntHdr.get().getLoaiVthh()) ? null : hashMapDmHh.get(qdKhlcntHdr.get().getLoaiVthh()));
                f.setQdKhlcntHdr(qdKhlcntHdr.get());
            }
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });
        return page;
    }

    @Override
    @Transactional
    public QdPdHsmt create(QdPdHsmtReq objReq) throws Exception {
        if (!StringUtils.isEmpty(objReq.getSoQd())) {
            Optional<QdPdHsmt> qOptional = qdPdHsmtRepository.findBySoQd(objReq.getSoQd());
            if (qOptional.isPresent()) {
                throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
            }
        }
        QdPdHsmt dataMap = new ModelMapper().map(objReq, QdPdHsmt.class);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(Contains.DANG_NHAP_DU_LIEU);
        dataMap.setNguoiTao(getUser().getUsername());
        QdPdHsmt created =  qdPdHsmtRepository.save(dataMap);
        if (!DataUtils.isNullOrEmpty(objReq.getListCcPhapLy())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getListCcPhapLy(), created.getId(), QdPdHsmt.TABLE_NAME + "_CAN_CU");
        }
        return created;
    }

    @Override
    @Transactional
    public QdPdHsmt update(QdPdHsmtReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<QdPdHsmt> qOptional = qdPdHsmtRepository.findById(objReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        if (!StringUtils.isEmpty(objReq.getSoQd())) {
            Optional<QdPdHsmt> deXuat = qdPdHsmtRepository.findBySoQd(objReq.getSoQd());
            if (deXuat.isPresent()) {
                if (!deXuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
                }
            }
        }
        QdPdHsmt dataDTB = qOptional.get();
        QdPdHsmt dataMap = ObjectMapperUtils.map(objReq, QdPdHsmt.class);
        updateObjectToObject(dataDTB, dataMap);
        dataDTB.setNgaySua(getDateTimeNow());
        dataDTB.setNguoiSua(getUser().getUsername());
        fileDinhKemService.delete(dataDTB.getId(), Lists.newArrayList(QdPdHsmt.TABLE_NAME + "_CAN_CU"));
        if (!DataUtils.isNullOrEmpty(objReq.getListCcPhapLy())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getListCcPhapLy(), dataDTB.getId(), QdPdHsmt.TABLE_NAME + "_CAN_CU");
        }
        return qdPdHsmtRepository.save(dataDTB);
    }

    @Override
    public QdPdHsmt detail(Long id) throws Exception {
        Optional<QdPdHsmt> qOptional = qdPdHsmtRepository.findById(id);
        if (!qOptional.isPresent()) {
            throw new Exception("Không tồn tại bản ghi");
        }
        Optional<HhQdKhlcntHdr> qdKhlcntHdr = hhQdKhlcntHdrRepository.findById(qOptional.get().getIdQdPdKhlcnt());
        if (!qdKhlcntHdr.isPresent()) {
            throw new Exception("Không tồn tại quyết định KH LCNT");
        }
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        qdKhlcntHdr.get().setTenLoaiVthh(StringUtils.isEmpty(qdKhlcntHdr.get().getLoaiVthh()) ? null : hashMapDmHh.get(qdKhlcntHdr.get().getLoaiVthh()));
        qdKhlcntHdr.get().setTenCloaiVthh(StringUtils.isEmpty(qdKhlcntHdr.get().getCloaiVthh()) ? null : hashMapDmHh.get(qdKhlcntHdr.get().getCloaiVthh()));
        qdKhlcntHdr.get().setTenDvi(mapDmucDvi.get(qdKhlcntHdr.get().getMaDvi()));
        List<HhQdKhlcntDsgthau> hhQdKhlcntDsgthauData = hhQdKhlcntDsgthauRepository.findByIdQdHdr(qdKhlcntHdr.get().getId());
        for(HhQdKhlcntDsgthau dsg : hhQdKhlcntDsgthauData){
            List<HhQdKhlcntDsgthauCtiet> listGtCtiet = hhQdKhlcntDsgthauCtietRepository.findByIdGoiThau(dsg.getId());
            listGtCtiet.forEach(f -> {
                f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
                f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
                List<HhQdKhlcntDsgthauCtietVt> byIdGoiThauCtiet = hhQdKhlcntDsgthauCtietVtRepository.findByIdGoiThauCtiet(f.getId());
                byIdGoiThauCtiet.forEach( x -> {
                    x.setTenDvi(mapDmucDvi.get(x.getMaDvi()));
                });
                f.setChildren(byIdGoiThauCtiet);
            });
            dsg.setTenDvi(mapDmucDvi.get(dsg.getMaDvi()));
            dsg.setTenCloaiVthh(hashMapDmHh.get(dsg.getCloaiVthh()));
            dsg.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dsg.getTrangThai()));
            dsg.setChildren(listGtCtiet);
        }
        qdKhlcntHdr.get().setDsGthau(hhQdKhlcntDsgthauData);
        qdKhlcntHdr.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qdKhlcntHdr.get().getTrangThai()));
        if (qdKhlcntHdr.get().getIdTrHdr() != null) {
            Optional<HhDxuatKhLcntHdr> dxuatKhLcntHdr = hhDxuatKhLcntHdrRepository.findById(qdKhlcntHdr.get().getIdTrHdr());
            dxuatKhLcntHdr.ifPresent(qdKhlcntHdr.get()::setDxKhlcntHdr);
        }
        if (qdKhlcntHdr.get().getDieuChinh().equals(Boolean.TRUE)) {
            Optional<HhDchinhDxKhLcntHdr> dchinhDxKhLcntHdr = hhDchinhDxKhLcntHdrRepository.findByIdQdGocAndLastest(qdKhlcntHdr.get().getId(), Boolean.TRUE);
            if (dchinhDxKhLcntHdr.isPresent()) {
                List<HhDchinhDxKhLcntDsgthau> gThauList = gThauRepository.findAllByIdDcDxHdr(dchinhDxKhLcntHdr.get().getId());
                for(HhDchinhDxKhLcntDsgthau gThau : gThauList){
                    List<HhDchinhDxKhLcntDsgthauCtiet> gthauCtietList = gThauCietRepository.findAllByIdGoiThau(gThau.getId());
                    gthauCtietList.forEach(f -> {
                        f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
                        f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
                        List<HhDchinhDxKhLcntDsgthauCtietVt> gthauCtietVtList = gThauCietVtRepository.findAllByIdGoiThauCtiet(f.getId());
                        gthauCtietVtList.forEach(ct ->{
                            ct.setTenDvi(mapDmucDvi.get(ct.getMaDvi()));
                        });
                        f.setChildren(gthauCtietVtList);
                    });
                    gThau.setTenCloaiVthh(hashMapDmHh.get(gThau.getCloaiVthh()));
                    gThau.setChildren(gthauCtietList);
                }
                dchinhDxKhLcntHdr.get().setDsGthau(gThauList);
                qdKhlcntHdr.get().setDchinhDxKhLcntHdr(dchinhDxKhLcntHdr.get());
                qdKhlcntHdr.get().setSoQdDc(dchinhDxKhLcntHdr.get().getSoQdDc());
            }
        }
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
        qOptional.get().setQdKhlcntHdr(qdKhlcntHdr.get());
        qOptional.get().setListCcPhapLy(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(QdPdHsmt.TABLE_NAME + "_CAN_CU")));
        return qOptional.get();
    }

    @Override
    @Transactional
    public QdPdHsmt approve(StatusReq stReq) throws Exception {
        if (StringUtils.isEmpty(stReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        QdPdHsmt detail = detail(stReq.getId());
        if(detail.getLoaiVthh().startsWith("02")){
            return approveVatTu(stReq,detail);
        }
        else{
            return null;
        }
    }

    @Override
    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId())) {
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        Optional<QdPdHsmt> qOptional = qdPdHsmtRepository.findById(idSearchReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        if (!qOptional.get().getTrangThai().equals(Contains.DANG_NHAP_DU_LIEU) && !qOptional.get().getTrangThai().equals(Contains.TUCHOI_LDV)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        qdPdHsmtRepository.delete(qOptional.get());
    }

    @Override
    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList())){
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        List<QdPdHsmt> listHsmt =  qdPdHsmtRepository.findAllByIdIn(idSearchReq.getIdList());
        qdPdHsmtRepository.deleteAll(listHsmt);
    }

    QdPdHsmt approveVatTu(StatusReq stReq, QdPdHsmt dataDB) throws Exception {
        String status = stReq.getTrangThai() + dataDB.getTrangThai();
        if ((Contains.BAN_HANH + Contains.DANG_NHAP_DU_LIEU).equals(status)) {
            dataDB.setNguoiPduyet(getUser().getUsername());
            dataDB.setNgayPduyet(getDateTimeNow());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        dataDB.setTrangThai(stReq.getTrangThai());
        return qdPdHsmtRepository.save(dataDB);
    }
}
