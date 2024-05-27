package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

//    @GetMapping("/join")
//    public String createForm(){
//        return "members/createMemberForm";
//    }

//    @PostMapping("/join")
//    public Long create(Member member){
//
////        System.out.println("member = " + member.getName());
//        System.out.println("member = " + member.toString());
//
//        return memberService.join(member);
//
//    }

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable){
          return memberService.findMembers(pageable);
//        model.addAttribute("members", members);
//        return "members/memberList";
    }

}
