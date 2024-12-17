//package backend.flutter.controller;
//
//import backend.flutter.entity.Skill;
//import backend.flutter.exception.IdInvalidException;
//import backend.flutter.service.SkillService;
//import backend.flutter.util.annotation.ApiMessage;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1")
//public class SkillController {
//    private final SkillService skillService;
//    public SkillController(SkillService skillService) {
//        this.skillService = skillService;
//    }
//
//    @PostMapping("skills")
//    @ApiMessage("Create skill")
//    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) throws IdInvalidException {
//        if (skill.getName() != null && this.skillService.isNameExist(skill.getName())) {
//            throw new IdInvalidException("Skill name = "+ skill.getName() + " da ton tai");
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(skill));
//    }
//
//    @PutMapping("/skills")
//    @ApiMessage("update skills")
//    public ResponseEntity<Skill> updateSkill(@RequestBody Skill skill) throws IdInvalidException {
//        Skill currentSkill = this.skillService.fetchSkillById(skill.getId());
//        if (currentSkill == null) {
//            throw new IdInvalidException("Skill id = " + skill.getId()+ " khong ton tai");
//        }
//        if (skill.getName() != null && this.skillService.isNameExist(skill.getName())) {
//            throw new IdInvalidException("Skill name = " + skill.getName() + " da ton tai");
//        }
//        currentSkill.setName(skill.getName());
//        return ResponseEntity.ok().body(this.skillService.updateSkill(currentSkill));
//    }
////    @GetMapping("/skills")
////    @ApiMessage("fetch all skill")
////    public ResponseEntity<ResultPagi>
//}
