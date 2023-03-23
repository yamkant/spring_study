package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); // static 사용
    private static long sequence = 0L; // static 사용

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    // NOTE: 못찾는 경우도 생각해야하므로, Optional로 반환합니다.
    public Optional<Member> findByLoginId(String loginId) {
    //  List<Member> all = findAll();
    //  for (Member m : all) {
    //      if (m.getLoginId().equals(loginId)) {
    //          return Optional.of(m);
    //      }
    //  }
    //  return Optional.empty();

        // Stream을 사용하여 위의 구문을 요약할 수 있습니다.
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll() {
        // NOTE: store에 있는 Map 구조의 값들의 value들 즉, Member들이 리스트로 변환되어 반환됩니다.
        return new ArrayList<>(store.values());
    }

}
