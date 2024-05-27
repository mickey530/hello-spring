package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    Optional<Member> findByUsername(String name);
    Optional<Member> findByEmail(String name);
    Page<Member> findAll(Pageable pageable);

}
