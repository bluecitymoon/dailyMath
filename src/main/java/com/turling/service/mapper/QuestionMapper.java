package com.turling.service.mapper;

import com.turling.domain.Question;
import com.turling.domain.QuestionCategory;
import com.turling.domain.QuestionType;
import com.turling.domain.QuestionOption;
import com.turling.domain.StudentGrade;
import com.turling.service.dto.QuestionCategoryDTO;
import com.turling.service.dto.QuestionDTO;
import com.turling.service.dto.QuestionTypeDTO;
import com.turling.service.dto.StudentGradeDTO;
import com.turling.service.dto.QuestionOptionDTO;
import org.mapstruct.*;
import java.util.List;
import java.util.Set;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionOptionMapper.class })
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "questionCategory", source = "questionCategory", qualifiedByName = "questionCategoryName")
    @Mapping(target = "type", source = "type", qualifiedByName = "questionTypeName")
    @Mapping(target = "grade", source = "grade", qualifiedByName = "studentGradeName")
    @Mapping(target = "options", source = "options")
    QuestionDTO toDto(Question s);

    List<QuestionOptionDTO> toQuestionOptionDTOList(Set<QuestionOption> options);
    Set<QuestionOption> toQuestionOptionSet(List<QuestionOptionDTO> options);

    @Named("questionCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    QuestionCategoryDTO toDtoQuestionCategoryName(QuestionCategory questionCategory);

    @Named("questionTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    QuestionTypeDTO toDtoQuestionTypeName(QuestionType questionType);

    @Named("studentGradeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StudentGradeDTO toDtoStudentGradeName(StudentGrade studentGrade);

    @AfterMapping
    default void linkOptions(@MappingTarget Question question) {
        if (question != null && question.getOptions() != null) {
            question.getOptions().forEach(option -> option.setQuestion(question));
        }
    }
}
