package backend.flutter.controller;

import backend.flutter.dto.request.RequestCreateCompany;
import backend.flutter.dto.response.ResponseResultPagination;
//import backend.flutter.dto.response.RestResponse;
import backend.flutter.entity.Company;
import backend.flutter.entity.User;
import backend.flutter.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class CompanyController {
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company request) {
        Company company = this.companyService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping("/companies")
    public ResponseEntity<ResponseResultPagination> getAllCompanies(
            @Filter Specification<Company> spec,
            Pageable pageable
    ) {
        return ResponseEntity.ok(companyService.getAllCompany(spec,pageable));
    }

    @PutMapping("/companies/{idCom}")
    public ResponseEntity<Company> updateCompany(@PathVariable long idCom, @RequestBody Company request) {
        return ResponseEntity.ok(companyService.updateCompany( idCom,request));
    }

    @DeleteMapping("/companies/{idCom}")
    public ResponseEntity<Void> deleteCompany(@PathVariable long idCom) {

        companyService.deleteCompany(idCom);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
