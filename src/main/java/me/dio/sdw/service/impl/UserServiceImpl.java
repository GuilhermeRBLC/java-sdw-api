package me.dio.sdw.service.impl;

import org.springframework.transaction.annotation.Transactional;
import me.dio.sdw.model.User;
import me.dio.sdw.repository.UserRepository;
import me.dio.sdw.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Optional.ofNullable;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return this.userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public User create(User userToCreate) {
        ofNullable(userToCreate).orElseThrow(() -> new RuntimeException("User to create must not be null."));
        ofNullable(userToCreate.getAccount()).orElseThrow(() -> new RuntimeException("User account must not be null."));
        ofNullable(userToCreate.getCard()).orElseThrow(() -> new RuntimeException("User card must not be null."));

        if (userRepository.existsByAccountNumber(userToCreate.getAccount().getNumber())) {
            throw new RuntimeException("This account number already exists.");
        }
        if (userRepository.existsByCardNumber(userToCreate.getCard().getNumber())) {
            throw new RuntimeException("This card number already exists.");
        }
        return this.userRepository.save(userToCreate);
    }

    @Transactional
    public User update(Long id, User userToUpdate) {
        User dbUser = this.findById(id);
        if (!dbUser.getId().equals(userToUpdate.getId())) {
            throw new RuntimeException("Update IDs must be the same.");
        }

        dbUser.setName(userToUpdate.getName());
        dbUser.setAccount(userToUpdate.getAccount());
        dbUser.setCard(userToUpdate.getCard());
        dbUser.setFeatures(userToUpdate.getFeatures());
        dbUser.setNews(userToUpdate.getNews());

        return this.userRepository.save(dbUser);
    }

    @Transactional
    public void delete(Long id) {
        User dbUser = this.findById(id);
        this.userRepository.delete(dbUser);
    }

}
