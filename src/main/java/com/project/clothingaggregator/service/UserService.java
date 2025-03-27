package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.UserRegistrationRequest;
import com.project.clothingaggregator.dto.UserUpdateRequest;
import com.project.clothingaggregator.dto.UserWithOrdersDto;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.UserMapper;
import com.project.clothingaggregator.model.UserModel;
import com.project.clothingaggregator.repository.OrderRepository;
import com.project.clothingaggregator.repository.UserRepository;
import com.project.clothingaggregator.security.PasswordUtil;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PasswordUtil passwordUtil;

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

//    public List<UserWithOrdersDto> getAllUsersWithOrdersAndProducts() {
//        List<User> users = userRepository.findAllWithOrdersAndProducts();
//        return users.stream()
//                .map(UserMapper::toUserWithOrdersDto)
//                .toList();
//    }

    public Page<UserWithOrdersDto> getAllUsersWithOrdersAndProducts(int page, int size) {
        // 1. Получаем страницу пользователей с заказами
        Page<User> users = userRepository.findAllWithOrders(PageRequest.of(page, size));

        // 2. Собираем ID всех заказов
        List<Integer> orderIds = users.stream()
                .flatMap(u -> u.getOrders().stream())
                .map(Order::getId)
                .collect(Collectors.toList());

        if (!orderIds.isEmpty()) {
            // 3. Загружаем заказы с элементами
            List<Order> ordersWithItems = orderRepository.findOrdersWithItems(orderIds);

            // 4. Создаем карту для быстрого доступа
            Map<Integer, Order> orderMap = ordersWithItems.stream()
                    .collect(Collectors.toMap(Order::getId, Function.identity()));

            // 5. Обновляем заказы у пользователей
            users.getContent().forEach(user ->
                    user.getOrders().forEach(order -> {
                        Order fullOrder = orderMap.get(order.getId());
                        if (fullOrder != null) {
                            order.setItems(fullOrder.getItems());
                        }
                    })
            );
        }

        // 6. Маппим в DTO
        return users.map(UserMapper::toUserWithOrdersDto);
    }

//    public Page<UserWithOrdersDto> getAllUsersWithOrdersAndProducts(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
//        Page<User> users = userRepository.findAllWithOrders(pageable);
//
//        // Догружаем items для заказов
//        List<Integer> orderIds = users.stream()
//                .flatMap(u -> u.getOrders().stream())
//                .map(Order::getId)
//                .collect(Collectors.toList());
//
//        if (!orderIds.isEmpty()) {
//            userRepository.findWithItemsByIdIn(orderIds);
//        }
//
//        return users.map(UserMapper::toUserWithOrdersDto);
//    }

}