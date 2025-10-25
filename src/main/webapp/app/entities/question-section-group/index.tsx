import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QuestionSectionGroup from './question-section-group';
import QuestionSectionGroupDetail from './question-section-group-detail';
import QuestionSectionGroupUpdate from './question-section-group-update';
import QuestionSectionGroupDeleteDialog from './question-section-group-delete-dialog';

const QuestionSectionGroupRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QuestionSectionGroup />} />
    <Route path="new" element={<QuestionSectionGroupUpdate />} />
    <Route path=":id">
      <Route index element={<QuestionSectionGroupDetail />} />
      <Route path="edit" element={<QuestionSectionGroupUpdate />} />
      <Route path="delete" element={<QuestionSectionGroupDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuestionSectionGroupRoutes;
