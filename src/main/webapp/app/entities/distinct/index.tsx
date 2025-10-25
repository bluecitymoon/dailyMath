import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Distinct from './distinct';
import DistinctDetail from './distinct-detail';
import DistinctUpdate from './distinct-update';
import DistinctDeleteDialog from './distinct-delete-dialog';

const DistinctRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Distinct />} />
    <Route path="new" element={<DistinctUpdate />} />
    <Route path=":id">
      <Route index element={<DistinctDetail />} />
      <Route path="edit" element={<DistinctUpdate />} />
      <Route path="delete" element={<DistinctDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DistinctRoutes;
