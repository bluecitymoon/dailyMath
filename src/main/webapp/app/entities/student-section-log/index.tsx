import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StudentSectionLog from './student-section-log';
import StudentSectionLogDetail from './student-section-log-detail';
import StudentSectionLogUpdate from './student-section-log-update';
import StudentSectionLogDeleteDialog from './student-section-log-delete-dialog';

const StudentSectionLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StudentSectionLog />} />
    <Route path="new" element={<StudentSectionLogUpdate />} />
    <Route path=":id">
      <Route index element={<StudentSectionLogDetail />} />
      <Route path="edit" element={<StudentSectionLogUpdate />} />
      <Route path="delete" element={<StudentSectionLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudentSectionLogRoutes;
