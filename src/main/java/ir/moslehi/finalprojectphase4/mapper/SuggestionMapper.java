package ir.moslehi.finalprojectphase4.mapper;

import ir.moslehi.finalprojectphase4.dto.suggestion.SuggestionSaveRequest;
import ir.moslehi.finalprojectphase4.dto.suggestion.SuggestionSaveResponse;
import ir.moslehi.finalprojectphase4.model.Suggestion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SuggestionMapper {

    SuggestionMapper INSTANCE = Mappers.getMapper(SuggestionMapper.class);

    Suggestion suggestionSaveRequestToModel(SuggestionSaveRequest request);

    SuggestionSaveResponse modelToSuggestionSaveResponse(Suggestion suggestion);

    List<SuggestionSaveResponse> modelListToSuggestionSaveResponseList(List<Suggestion> suggestionList);
}
