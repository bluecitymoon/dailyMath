import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QuestionBaseGroup from './question-base-group';
import QuestionBaseGroupDetail from './question-base-group-detail';
import QuestionBaseGroupUpdate from './question-base-group-update';
import QuestionBaseGroupDeleteDialog from './question-base-group-delete-dialog';

const QuestionBaseGroupRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QuestionBaseGroup />} />
    <Route path="new" element={<QuestionBaseGroupUpdate />} />
    <Route path=":id">
      <Route index element={<QuestionBaseGroupDetail />} />
      <Route path="edit" element={<QuestionBaseGroupUpdate />} />
      <Route path="delete" element={<QuestionBaseGroupDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuestionBaseGroupRoutes;
