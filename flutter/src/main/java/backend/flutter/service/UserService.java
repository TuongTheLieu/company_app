package backend.flutter.service;

import backend.flutter.dto.request.RequestCreateUser;
import backend.flutter.dto.request.RequestUpdateUser;
import backend.flutter.dto.response.*;
import backend.flutter.entity.Company;
import backend.flutter.entity.User;
import backend.flutter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired CompanyService companyService;
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        //check company
        if(user.getCompany() != null){
            Optional<Company> company = this.companyService.findById(user.getCompany().getId());
            user.setCompany(company.isPresent() ? company.get() : null);
        }
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        this.userRepository.deleteById(userId);

    }

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ResponseResultPagination getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = this.userRepository.findAll(spec, pageable);
        ResponseResultPagination response = new ResponseResultPagination();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUsers.getTotalPages());
        meta.setTotal(pageUsers.getTotalElements());
        response.setMeta(meta);
        //remove sensitive data
        List<ResponseUser> listUser = pageUsers.getContent()
                .stream().map(item -> new ResponseUser(
                        item.getId(),
                        item.getEmail(),
                        item.getUsername(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreateAt(),
                        new ResponseUser.CompanyUser(
                                item.getCompany() != null ? item.getCompany().getId() : 0,
                                item.getCompany() != null ? item.getCompany().getName() : null
                        )
                        )
                )

                .collect(Collectors.toList());
        response.setResult(listUser);
        return response;
    }
    public User updateUser(User request){
        User currentUser = this.getUserById(String.valueOf(request.getId()));
        if (currentUser != null){
            currentUser.setAddress(request.getAddress());
            currentUser.setUsername(request.getUsername());
            currentUser.setAge(request.getAge());
            currentUser.setGender(request.getGender());

            //check company
            if (request.getCompany() != null){
                Optional<Company> company = this.companyService.findById(request.getCompany().getId());
                request.setCompany(company.isPresent() ? company.get() : null);
            }

            currentUser = this.userRepository.save(currentUser);
        }

        return currentUser;
    }
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }
    public boolean isIdExist(String userId) {
        return userRepository.existsById(userId);
    }
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
    public ResponseCreateUser responseCreateUser(User user) {
        ResponseCreateUser res = new ResponseCreateUser();
        ResponseCreateUser.CompanyUser com = new ResponseCreateUser.CompanyUser();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreateAt());
        if (user.getCompany() != null){
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }
        return res;

    }

    public ResponseUser responseUser(User user) {
        ResponseUser res = new ResponseUser();

        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreateAt());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }
    public ResponseUpdateUser responseUpdateUser(User user) {
        ResponseUpdateUser res = new ResponseUpdateUser();
        ResponseUpdateUser.CompanyUser com = new ResponseUpdateUser.CompanyUser();
        if (user.getCompany() != null){
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setAddress(user.getAddress());
        res.setAge(String.valueOf(user.getAge()));
        res.setGender(user.getGender());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;

    }
    public void updateUserToken(String token, String email ) {
        User currentUser = this.getUserByEmail(email);
        if (currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
    public User getUserByRefreshTokenAndEmail(String refresh_token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refresh_token, email);
    }

}
