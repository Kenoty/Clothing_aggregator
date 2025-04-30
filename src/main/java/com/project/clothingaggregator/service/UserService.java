package com.project.clothingaggregator.service;

import com.project.clothingaggregator.cache.MyCache;
import com.project.clothingaggregator.dto.UserRegistrationRequest;
import com.project.clothingaggregator.dto.UserUpdateRequest;
import com.project.clothingaggregator.dto.UserWithFavoritesDto;
import com.project.clothingaggregator.dto.UserWithOrdersDto;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.UserMapper;
import com.project.clothingaggregator.repository.OrderRepository;
import com.project.clothingaggregator.repository.UserFavoriteRepository;
import com.project.clothingaggregator.repository.UserRepository;
import com.project.clothingaggregator.security.PasswordUtil;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final PasswordUtil passwordUtil;
    private final MyCache<Integer, UserWithFavoritesDto> cache = new MyCache<>(100);

    public User createUser(UserRegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setBirthday(request.getBirthday());
        user.setEmail(request.getEmail());
        user.setPassword(passwordUtil.hashPassword(request.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Integer id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setUsername(updateRequest.getUsername());
        user.setBirthday(updateRequest.getBirthday());
        user.setEmail(updateRequest.getEmail());

        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public List<UserWithOrdersDto> getAllUsersWithOrdersAndItems() {
        List<User> users = userRepository.findAllWithOrders();

        List<Integer> orderIds = users.stream()
                .flatMap(u -> u.getOrders().stream())
                .map(Order::getId)
                .collect(Collectors.toList());

        if (!orderIds.isEmpty()) {
            List<Order> ordersWithItems = orderRepository.findOrdersWithItems(orderIds);

            Map<Integer, Order> orderMap = ordersWithItems.stream()
                    .collect(Collectors.toMap(Order::getId, Function.identity()));

            users.forEach(user ->
                    user.getOrders().forEach(order -> {
                        Order fullOrder = orderMap.get(order.getId());
                        if (fullOrder != null) {
                            order.setItems(fullOrder.getItems());
                        }
                    })
            );
        }

        return users.stream().map(UserMapper::toUserWithOrdersDto).toList();
    }

    public List<UserWithFavoritesDto> getUsersByBrand(String brandName) {
        return userRepository.getAllByFavoriteBrand(brandName).stream()
                .map(user -> {
                    UserWithFavoritesDto cached = cache.get(user.getId());
                    if (cached != null) {
                        System.out.println("User " + user.getId() + " taken from cache");
                        return cached;
                    }

                    user.setFavorites(userFavoriteRepository.findAllByUserId(user.getId()));
                    UserWithFavoritesDto dto = UserMapper.toUserWithFavorites(user);
                    cache.put(user.getId(), dto);
                    return dto;
                })
                .toList();
    }

}