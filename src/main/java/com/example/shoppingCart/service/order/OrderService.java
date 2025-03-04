package com.example.shoppingCart.service.order;

import com.example.shoppingCart.dto.OrderDto;
import com.example.shoppingCart.enums.OrderStatus;
import com.example.shoppingCart.exception.ResourceNotFoundException;
import com.example.shoppingCart.model.Cart;
import com.example.shoppingCart.model.Order;
import com.example.shoppingCart.model.OrderItem;
import com.example.shoppingCart.model.Product;
import com.example.shoppingCart.repository.OrderRepository;
import com.example.shoppingCart.repository.ProductRepository;
import com.example.shoppingCart.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order =createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order,cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream()
                .map(item-> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
    private Order createOrder(Cart cart){
        Order order= new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems (Order order , Cart cart){
        return cart.getItems().stream()
                .map(cartItem ->{
                    Product product =cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice()
                    );
                }).toList();
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found"));
    }
    @Override
    public List<OrderDto> getUserOrders(Long userId){
      List<Order> orders = orderRepository.findByUserId(userId);
      return orders.stream().map(this::convertToDto).toList();
    }

     @Override
    public OrderDto convertToDto (Order order){
        return modelMapper.map(order,OrderDto.class);
    }

}
