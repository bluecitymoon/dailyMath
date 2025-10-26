import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './student-section-log.reducer';

export const StudentSectionLog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const studentSectionLogList = useAppSelector(state => state.studentSectionLog.entities);
  const loading = useAppSelector(state => state.studentSectionLog.loading);
  const totalItems = useAppSelector(state => state.studentSectionLog.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="student-section-log-heading" data-cy="StudentSectionLogHeading">
        Student Section Logs
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link
            to="/student-section-log/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; 创建新 Student Section Log
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {studentSectionLogList && studentSectionLogList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('studentId')}>
                  Student Id <FontAwesomeIcon icon={getSortIconByFieldName('studentId')} />
                </th>
                <th className="hand" onClick={sort('sectionId')}>
                  Section Id <FontAwesomeIcon icon={getSortIconByFieldName('sectionId')} />
                </th>
                <th className="hand" onClick={sort('totalCount')}>
                  Total Count <FontAwesomeIcon icon={getSortIconByFieldName('totalCount')} />
                </th>
                <th className="hand" onClick={sort('finishedCount')}>
                  Finished Count <FontAwesomeIcon icon={getSortIconByFieldName('finishedCount')} />
                </th>
                <th className="hand" onClick={sort('correctRate')}>
                  Correct Rate <FontAwesomeIcon icon={getSortIconByFieldName('correctRate')} />
                </th>
                <th className="hand" onClick={sort('createDate')}>
                  Create Date <FontAwesomeIcon icon={getSortIconByFieldName('createDate')} />
                </th>
                <th className="hand" onClick={sort('updateDate')}>
                  Update Date <FontAwesomeIcon icon={getSortIconByFieldName('updateDate')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {studentSectionLogList.map((studentSectionLog, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/student-section-log/${studentSectionLog.id}`} color="link" size="sm">
                      {studentSectionLog.id}
                    </Button>
                  </td>
                  <td>{studentSectionLog.studentId}</td>
                  <td>{studentSectionLog.sectionId}</td>
                  <td>{studentSectionLog.totalCount}</td>
                  <td>{studentSectionLog.finishedCount}</td>
                  <td>{studentSectionLog.correctRate}</td>
                  <td>
                    {studentSectionLog.createDate ? (
                      <TextFormat type="date" value={studentSectionLog.createDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {studentSectionLog.updateDate ? (
                      <TextFormat type="date" value={studentSectionLog.updateDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/student-section-log/${studentSectionLog.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">查看</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/student-section-log/${studentSectionLog.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/student-section-log/${studentSectionLog.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">删除</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Student Section Logs found</div>
        )}
      </div>
      {totalItems ? (
        <div className={studentSectionLogList && studentSectionLogList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default StudentSectionLog;
