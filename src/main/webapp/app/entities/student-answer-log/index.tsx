import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StudentAnswerLog from './student-answer-log';
import StudentAnswerLogDetail from './student-answer-log-detail';

const StudentAnswerLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StudentAnswerLog />} />
    <Route path=":id">
      <Route index element={<StudentAnswerLogDetail />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudentAnswerLogRoutes;
