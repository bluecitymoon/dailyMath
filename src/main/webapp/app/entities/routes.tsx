import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QuestionCategory from './question-category';
import StudentGrade from './student-grade';
import QuestionType from './question-type';
import QuestionOption from './question-option';
import Question from './question';
import Distinct from './distinct';
import Community from './community';
import School from './school';
import Student from './student';
import QuestionBaseGroup from './question-base-group';
import QuestionSectionGroup from './question-section-group';
import StudentAnswerLog from './student-answer-log';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="question-category/*" element={<QuestionCategory />} />
        <Route path="student-grade/*" element={<StudentGrade />} />
        <Route path="question-type/*" element={<QuestionType />} />
        <Route path="question-option/*" element={<QuestionOption />} />
        <Route path="question/*" element={<Question />} />
        <Route path="distinct/*" element={<Distinct />} />
        <Route path="community/*" element={<Community />} />
        <Route path="school/*" element={<School />} />
        <Route path="student/*" element={<Student />} />
        <Route path="question-base-group/*" element={<QuestionBaseGroup />} />
        <Route path="question-section-group/*" element={<QuestionSectionGroup />} />
        <Route path="student-answer-log/*" element={<StudentAnswerLog />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
