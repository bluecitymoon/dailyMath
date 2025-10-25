import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QuestionCategory from './question-category';
import QuestionCategoryDetail from './question-category-detail';
import QuestionCategoryUpdate from './question-category-update';
import QuestionCategoryDeleteDialog from './question-category-delete-dialog';

const QuestionCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QuestionCategory />} />
    <Route path="new" element={<QuestionCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<QuestionCategoryDetail />} />
      <Route path="edit" element={<QuestionCategoryUpdate />} />
      <Route path="delete" element={<QuestionCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuestionCategoryRoutes;
