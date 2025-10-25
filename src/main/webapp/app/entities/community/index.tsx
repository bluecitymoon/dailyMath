import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Community from './community';
import CommunityDetail from './community-detail';
import CommunityUpdate from './community-update';
import CommunityDeleteDialog from './community-delete-dialog';

const CommunityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Community />} />
    <Route path="new" element={<CommunityUpdate />} />
    <Route path=":id">
      <Route index element={<CommunityDetail />} />
      <Route path="edit" element={<CommunityUpdate />} />
      <Route path="delete" element={<CommunityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CommunityRoutes;
