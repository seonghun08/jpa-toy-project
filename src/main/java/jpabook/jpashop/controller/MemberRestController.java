package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.member.dto.MemberDto;
import jpabook.jpashop.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public MemberResponse findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        return new MemberResponse(member);
    }

    @GetMapping("/v2/members/{id}")
    public MemberResponse findMember(@PathVariable("id") Member member) {
        return new MemberResponse(member);
    }

    @GetMapping("/page/members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "name") Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page.map(MemberDto::new);
    }

    // @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i));
        }
    }
}
