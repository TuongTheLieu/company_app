//package backend.flutter.service;
//
//import backend.flutter.entity.Skill;
//import backend.flutter.repository.SkillRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class SkillService {
//    @Autowired
//    private SkillRepository skillRepository;
//
//    public boolean isNameExist(String name){
//        return this.skillRepository.existByName(name);
//    }
//
//    public Skill fetchSkillById(long id) {
//        Optional<Skill> skillOptional = this.skillRepository.findById(id);
//        if (skillOptional.isPresent()) {
//            return skillOptional.get();
//        }
//        return null;
//    }
//
//    public Skill createSkill(Skill skill) {
//        return this.skillRepository.save(skill);
//    }
//    public Skill updateSkill(Skill skill) {
//        return this.skillRepository.save(skill);
//    }
//
//
//}
