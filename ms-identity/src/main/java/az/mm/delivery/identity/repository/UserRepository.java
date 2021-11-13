package az.mm.delivery.identity.repository;

import az.mm.delivery.common.enums.UserType;
import az.mm.delivery.identity.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select distinct user from User user left join fetch user.roles",
            countQuery = "select count(distinct user) from User user")
    Page<User> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct user from User user left join fetch user.roles")
    List<User> findAllWithEagerRelationships();

    @Query("select user from User user left join fetch user.roles where user.id =:id")
    Optional<User> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select user from User user left join fetch user.roles where user.id =:id and user.username =:username")
    Optional<User> findByIdAndUsername(Long id, String username);

    @Query("select distinct user from User user left join fetch user.roles where user.type =:type")
    List<User> findAllByType(@Param("type") UserType type);

    Optional<User> findByUsernameEqualsIgnoreCase(String username);

    boolean existsByUsername(String username);

}
