import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StudentGrade from './student-grade';
import StudentGradeDetail from './student-grade-detail';
import StudentGradeUpdate from './student-grade-update';
import StudentGradeDeleteDialog from './student-grade-delete-dialog';

const StudentGradeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StudentGrade />} />
    <Route path="new" element={<StudentGradeUpdate />} />
    <Route path=":id">
      <Route index element={<StudentGradeDetail />} />
      <Route path="edit" element={<StudentGradeUpdate />} />
      <Route path="delete" element={<StudentGradeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudentGradeRoutes;
