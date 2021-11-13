package az.mm.delivery.identity.service;

import az.mm.delivery.common.enums.UserType;
import az.mm.delivery.common.error.exception.InvalidInputException;
import az.mm.delivery.common.security.util.SecurityUtil;
import az.mm.delivery.identity.domain.User;
import az.mm.delivery.identity.dto.UserDto;
import az.mm.delivery.identity.error.validation.ValidationMessage;
import az.mm.delivery.identity.mapper.UserMapper;
import az.mm.delivery.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static az.mm.delivery.common.security.util.SecurityUtil.getCurrentUserLogin;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto save(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAllWithEagerRelationships().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAllCouriers() {
        return userRepository.findAllByType(UserType.COURIER).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto findOne(Long id) {
        Optional<User> user = SecurityUtil.hasAdminRole() ? userRepository.findOneWithEagerRelationships(id) :
                userRepository.findByIdAndUsername(id, getCurrentUserLogin());
        return user.map(userMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.USER_NOT_FOUND, List.of(id)));
    }

    @Transactional(readOnly = true)
    public boolean isUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
