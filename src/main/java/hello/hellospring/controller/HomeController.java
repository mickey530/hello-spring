package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    private final MemberService memberService;

    @Autowired
    public HomeController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/tables")
    public String getTableList() {
        return "tables";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }
    @GetMapping("/join")
    public String join(Model model){
        model.addAttribute("member", new Member());
        return "join";
    }

    @PostMapping("/join")
    public String registerUser(@ModelAttribute("member") Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            memberService.registerUser(member);
        } catch (IllegalStateException e) {
            bindingResult.rejectValue("username", "error.user", e.getMessage());
            return "register";
        }

        return "redirect:/login";
    }

}
