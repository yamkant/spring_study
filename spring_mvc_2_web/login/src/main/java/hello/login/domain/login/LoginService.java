package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null이면 로그인 실패
     */
    public Member login(String loginId, String password) {
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//        Member member = findMemberOptional.get();
//        if (member.getPassword().equals(password)) {
//            return member;
//        } else {
//            return null;
//        }

        // NOTE: 위 로직을 다음과 같이 Optional Stream으로 사용할 수 있습니다.
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
