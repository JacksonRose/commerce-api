package com.example.commerceapi.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.commerceapi.exception.UserExceptions;
import com.example.commerceapi.user.document.Address;
import com.example.commerceapi.user.document.UserDocument;
import com.example.commerceapi.user.document.WishList;
import com.example.commerceapi.user.dto.AddressRequest;
import com.example.commerceapi.user.dto.UserRequest;
import com.example.commerceapi.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional (propagation = Propagation.REQUIRES_NEW, timeout = 2)
    public UserDocument createUser(UserRequest request) {
        userRepository.findByEmail(request.getEmail())
            .ifPresent((value) -> {throw new UserExceptions.UserAlreadyExistsException(
                    request.getEmail());});
        UserDocument newUser = new UserDocument(
                request.getName(),
                request.getEmail(),
                request.getPhone());
        if (request.getAddresses() != null) {
            for (AddressRequest addressRequest : request.getAddresses()) {
                Address add = new Address();
                add.setCity(addressRequest.getCity());
                add.setType(addressRequest.getType());
                newUser.addAddress(add);
            }
        }
        if (request.getWishlists() != null) {
            request.getWishlists().stream()
                    .map(wishlist -> {
                        WishList wlist = new WishList();
                        wlist.setName(wishlist.getName());
                        return wlist;
                    }).forEach(newUser::addWishList);
        }
        return userRepository.save(newUser);
    }

    @Transactional(timeout = 2, readOnly = true)
    public UserDocument getUserByEmail(final String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserExceptions.UserNotFoundException(email));
    }

    @Transactional(timeout = 2, readOnly = true)
    public UserDocument getUserById(final String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserExceptions.UserNotFoundException(id));
    }
}
