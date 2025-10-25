import questionCategory from 'app/entities/question-category/question-category.reducer';
import studentGrade from 'app/entities/student-grade/student-grade.reducer';
import questionType from 'app/entities/question-type/question-type.reducer';
import questionOption from 'app/entities/question-option/question-option.reducer';
import question from 'app/entities/question/question.reducer';
import distinct from 'app/entities/distinct/distinct.reducer';
import community from 'app/entities/community/community.reducer';
import school from 'app/entities/school/school.reducer';
import student from 'app/entities/student/student.reducer';
import questionBaseGroup from 'app/entities/question-base-group/question-base-group.reducer';
import questionSectionGroup from 'app/entities/question-section-group/question-section-group.reducer';
import studentAnswerLog from 'app/entities/student-answer-log/student-answer-log.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  questionCategory,
  studentGrade,
  questionType,
  questionOption,
  question,
  distinct,
  community,
  school,
  student,
  questionBaseGroup,
  questionSectionGroup,
  studentAnswerLog,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
