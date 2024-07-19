package ir.moslehi.finalprojectphase4.mapper;

import ir.moslehi.finalprojectphase4.dto.comment.CommentListForExpert;
import ir.moslehi.finalprojectphase4.dto.comment.CommentSaveRequest;
import ir.moslehi.finalprojectphase4.dto.comment.CommentSaveResponse;
import ir.moslehi.finalprojectphase4.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment commentSaveRequestToModel(CommentSaveRequest response);

    CommentSaveResponse modelToCommentSaveResponse(Comment comment);

    List<CommentListForExpert> modelToCommentListForExpert(List<Comment> commentList);

}
