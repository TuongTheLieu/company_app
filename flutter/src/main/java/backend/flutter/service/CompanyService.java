package backend.flutter.service;

import backend.flutter.dto.request.RequestCreateCompany;
import backend.flutter.dto.response.Meta;
import backend.flutter.dto.response.ResponseResultPagination;
import backend.flutter.entity.Company;
import backend.flutter.entity.User;
import backend.flutter.repository.CompanyRepository;
import backend.flutter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository repository;
    @Autowired
    private UserRepository userRepository;
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company createCompany(Company request) {
        return companyRepository.save(request);
    }
    public ResponseResultPagination getAllCompany(Specification spec, Pageable pageable) {
        Page<Company> companyPage = this.companyRepository.findAll(pageable);
        ResponseResultPagination response = new ResponseResultPagination();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(companyPage.getTotalPages());
        meta.setTotal(companyPage.getTotalElements());

        response.setMeta(meta);
        response.setResult(companyPage.getContent());

        return response;
    }
    public Company updateCompany(long idCom, Company request) {
        Company updateCom = getCompany(idCom);
        updateCom.setName(request.getName());
        updateCom.setAddress(request.getAddress());
        updateCom.setLogo(request.getLogo());
        updateCom.setDescription(request.getDescription());

        return companyRepository.save(updateCom);
    }
    public Company getCompany(long idCom) {
        return companyRepository.findById(idCom).orElseThrow(() -> new RuntimeException("Comany not found"));
    }
    public void deleteCompany(long idCom) {
        Optional<Company> comOptional = this.companyRepository.findById(idCom);
        if (comOptional.isPresent()) {
            Company company = comOptional.get();
            List<User> users = this.userRepository.findByCompany(company);
            this.userRepository.deleteAll(users);
        }
        this.companyRepository.deleteById(idCom);
    }
    public Optional<Company> findById (long id){
       return companyRepository.findById(id);
    }

}
