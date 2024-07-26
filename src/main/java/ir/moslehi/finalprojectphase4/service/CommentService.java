package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.exception.DuplicateInformationException;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.exception.NotValidInput;
import ir.moslehi.finalprojectphase4.model.*;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import ir.moslehi.finalprojectphase4.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final SuggestionService suggestionService;
    private final CustomerService customerService;
    private final ExpertService expertService;

    public Comment save(Comment comment, String customerEmail) {
        Customer customer = customerService.findByEmail(customerEmail);
        Expert expert = expertService.findByEmail(comment.getExpert().getEmail());
        Orders orders = suggestionService.
                validOrderForCustomerWithOrderStatus(OrderStatus.PAID, customer, comment.getOrders().getId());
        if (!orders.getExpert().equals(expert))
            throw new NotValidInput("the expert isn't valid for this order");
        if (commentRepository.findByOrders(orders).isPresent())
            throw new DuplicateInformationException("this comment for this order have been saved");
        comment.setExpert(expert);
        comment.setCustomer(customer);
        return commentRepository.save(comment);
    }

    public List<Comment> findByExpert(Expert expert) {
        if (commentRepository.findByExpert(expert).isEmpty())
            throw new NotFoundException("there isn't any comment for expert by Id : " + expert.getId());
        return commentRepository.findByExpert(expert);
    }

}
