package hello.itemservice.domain.member;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);

        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        List<Member> all = findAll();
        for (Member m: all) {
            if (m.getLoginId().equals(loginId)) {
                return Optional.of(m); //회원등록된 로그인아이디 (== return m)
            }
        }
        return Optional.empty(); //회원등록되지 않은 로그인아이디 (== return null)
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}
