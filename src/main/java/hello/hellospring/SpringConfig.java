package hello.hellospring;

//import hello.hellospring.repository.JpaMemberRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;



    @Autowired
    public SpringConfig(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository, passwordEncoder);
    }

}
