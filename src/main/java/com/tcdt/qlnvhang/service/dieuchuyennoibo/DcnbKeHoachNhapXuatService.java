package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBBNTBQHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBienBanLayMauHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbKeHoachNhapXuatRepository;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachNhapXuat;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DcnbKeHoachNhapXuatService extends BaseServiceImpl {

    @Autowired
    private DcnbKeHoachNhapXuatRepository hdrRepository;

    @Autowired
    private DcnbBienBanLayMauHdrRepository dcnbBienBanLayMauHdrRepository;

    @Autowired
    private DcnbBBNTBQHdrRepository dcnbBBNTBQHdrRepository;

    @Transactional
    public DcnbKeHoachNhapXuat saveOrUpdate(DcnbKeHoachNhapXuat objReq) throws Exception {
        if(Objects.isNull(objReq.getIdKhDcDtl())){
            throw new Exception("IdKhDcDtl is null");
        }else if(Objects.isNull(objReq.getIdHdr())) {
            throw new Exception("IdHdr is null");
        } else if (DataUtils.isNullOrEmpty(objReq.getTableName())){
            throw new Exception("Table name is null");
        }
        if(checkHasKeHoachNx(objReq.getIdKhDcDtl(),objReq.getTableName())){
            throw new Exception("ID KH DTL : "+objReq.getIdKhDcDtl()+", Table Name :"+ objReq.getTableName()+" already exist");
        }
        DcnbKeHoachNhapXuat save = hdrRepository.save(objReq);
        return save;
    }

    public DcnbKeHoachNhapXuat detail(Long id) throws Exception {
        Optional<DcnbKeHoachNhapXuat> byId = hdrRepository.findById(id);
        if(byId.isPresent()){
            return byId.get();
        }else{
            throw new Exception("Data is null");
        }
    }

    public DcnbKeHoachNhapXuat detailKhDtl(Long idKhDtl) throws Exception {
        DcnbKeHoachNhapXuat keHoachNhapXuat = new DcnbKeHoachNhapXuat();
        List<DcnbKeHoachNhapXuat> idKhDcDtlList = hdrRepository.findByIdKhDcDtl(idKhDtl);
        if(idKhDcDtlList != null && !idKhDcDtlList.isEmpty()){
            keHoachNhapXuat.setIdKhDcDtl(idKhDtl);
            // Loop để set cái dữ liệu HDR vào 1 Kế hoạch nhập xuất theo Table Name
            idKhDcDtlList.forEach( khDtl -> {
                //Exam
                switch (khDtl.getTableName()){
                    // Biên bản lấy mẫu
                    case DcnbBienBanLayMauHdr.TABLE_NAME:
                        Optional<DcnbBienBanLayMauHdr> dcnbBienBanLayMauHdr = dcnbBienBanLayMauHdrRepository.findById(khDtl.getIdHdr());
                        dcnbBienBanLayMauHdr.ifPresent(khDtl::setDcnbBienBanLayMauHdr);
                        break;
                    // Biên bản .....
                    case DcnbBBNTBQHdr.TABLE_NAME:
                        Optional<DcnbBBNTBQHdr> dcnbBBNTBQHdr = dcnbBBNTBQHdrRepository.findById(khDtl.getIdHdr());
                        dcnbBBNTBQHdr.ifPresent(khDtl::setDcnbBBNTBQHdr);
                    default:
                        break;
                }
            });
        }
        return keHoachNhapXuat;
    }

    public boolean checkHasKeHoachNx(Long idKhDtl,String tableName){
        Optional<DcnbKeHoachNhapXuat> byIdKhDcDtlAndTableName = hdrRepository.findByIdKhDcDtlAndTableName(idKhDtl, tableName);
        if(byIdKhDcDtlAndTableName.isPresent()){
            return true;
        }
        return false;
    }

}
