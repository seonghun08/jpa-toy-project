package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@DynamicInsert // DynamicInsert: 초기화 되지 않은 필드는 insert 쿼리에서 제외된다. (성능 개선)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@ToString(of = {"id", "name", "age"})
@NamedQuery( // 잘 사용하지는 않음 (엔티티는 엔티티 만, 쿼리는 지양한다.)
        name = "Member.findByNames",
        query = "select m from Member m where m.name = :name"
)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username")
    private String name;

    @Column(name = "age")
    private int age;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @Builder
    public Member(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Member(String name) {
        this.name = name;
    }

    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Member(String name, int age, Team team) {
        this.name = name;
        this.age = age;

        if (team == null) {
            throw new IllegalArgumentException("team is null!!");
        }

        changeTeam(team);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void changeTeam(Team team) {
        team.getMembers().add(this);
        this.team = team;
    }
}
