package backend.flutter.controller;

import backend.flutter.dto.request.RequestCreateUser;
import backend.flutter.dto.request.RequestUpdateUser;
import backend.flutter.dto.response.ResponseCreateUser;
import backend.flutter.dto.response.ResponseResultPagination;
import backend.flutter.dto.response.ResponseUser;
import backend.flutter.entity.User;
import backend.flutter.exception.IdInvalidException;
import backend.flutter.repository.UserRepository;
import backend.flutter.service.UserService;
import backend.flutter.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping("/user")
    @ApiMessage("Tạo mới người dùng thành công")
    public ResponseEntity<ResponseCreateUser> createUser(@Valid @RequestBody User user, BindingResult result) throws IdInvalidException {

        boolean isEmailExist = this.userService.isEmailExist(user.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email "+user.getEmail() + " đã tồn tại"
            );
        }
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User userCreated = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.responseCreateUser(userCreated));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseUser> getUserId(@PathVariable String userId) throws IdInvalidException {
        User fetchedUser = this.userService.getUserById(userId);
        if (fetchedUser == null) {
            throw new IdInvalidException("User với id "+userId + " không tồn tại");
        }
            return ResponseEntity.status(HttpStatus.OK).body(this.userService.responseUser(fetchedUser));
    }

    @GetMapping("/user")
    @ApiMessage("fetch all user")
    public ResponseEntity<ResponseResultPagination> getAllUsers(
            @Filter Specification<User> spec,
            Pageable pageable
            ) {
        return ResponseEntity.ok(userService.getAllUsers(spec, pageable));
    }
    @DeleteMapping("/user/{userId}")
    @ApiMessage("Xóa thành công")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) throws IdInvalidException {
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            throw new IdInvalidException("User với id "+currentUser +" không tồn tại");
        }
        this.userService.deleteUser(userId);
        return ResponseEntity.ok(null);
    }
    @PutMapping("/user")
    @ApiMessage("Cập nhật ngươời dùng")
    public ResponseEntity<ResponseUser> updateUser( @RequestBody User request) throws IdInvalidException {
        User lieuUser = this.userService.updateUser(request);
        if (lieuUser == null) {
            throw new IdInvalidException("User với id"+request.getId()+ " không tồn tại");
        }
        return ResponseEntity.ok(this.userService.responseUser(lieuUser));
    }

}
