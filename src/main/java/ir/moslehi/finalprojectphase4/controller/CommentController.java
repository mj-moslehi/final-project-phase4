package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.comment.CommentListForExpert;
import ir.moslehi.finalprojectphase4.dto.comment.CommentSaveRequest;
import ir.moslehi.finalprojectphase4.dto.comment.CommentSaveResponse;
import ir.moslehi.finalprojectphase4.mapper.CommentMapper;
import ir.moslehi.finalprojectphase4.model.Comment;
import ir.moslehi.finalprojectphase4.model.Expert;
import ir.moslehi.finalprojectphase4.service.CommentService;
import ir.moslehi.finalprojectphase4.service.ExpertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class CommentController {

    private final CommentService commentService;
    private final ExpertService expertService;

    //todo
    @PostMapping("/register-comment")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CommentSaveResponse> registerComment(@Valid @RequestBody CommentSaveRequest request) {
        Comment mapper = CommentMapper.INSTANCE.commentSaveRequestToModel(request);
        Comment comment = commentService.save(mapper);
        expertService.updateScoreWithCommentScore(comment.getExpert(),comment.getScore());
        return new ResponseEntity<>(CommentMapper.INSTANCE.modelToCommentSaveResponse(comment), HttpStatus.CREATED);
    }

    //todo
    @GetMapping("/show-comments-for-expert")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<List<CommentListForExpert>> showComment(Principal principal){
        Expert expert = expertService.findByEmail(principal.getName());
        List<Comment> commentList = commentService.findByExpert(expert);
        return new ResponseEntity<>(CommentMapper.INSTANCE.modelToCommentListForExpert(commentList),HttpStatus.OK);
    }

}
