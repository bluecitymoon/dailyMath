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

import { getEntities } from './student.reducer';
import EmptyStateMessage from 'app/shared/components/empty-state-message';

export const Student = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const studentList = useAppSelector(state => state.student.entities);
  const loading = useAppSelector(state => state.student.loading);
  const totalItems = useAppSelector(state => state.student.totalItems);

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
      <h2 id="student-heading" data-cy="StudentHeading">
        学生管理
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> 刷新列表
          </Button>
          <Link to="/student/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; 创建新学生
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {studentList && studentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  姓名 <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('gender')}>
                  性别 <FontAwesomeIcon icon={getSortIconByFieldName('gender')} />
                </th>
                <th className="hand" onClick={sort('birthday')}>
                  生日 <FontAwesomeIcon icon={getSortIconByFieldName('birthday')} />
                </th>
                <th className="hand" onClick={sort('registerDate')}>
                  注册日期 <FontAwesomeIcon icon={getSortIconByFieldName('registerDate')} />
                </th>
                <th className="hand" onClick={sort('updateDate')}>
                  更新日期 <FontAwesomeIcon icon={getSortIconByFieldName('updateDate')} />
                </th>
                <th className="hand" onClick={sort('latestContractEndDate')}>
                  最新合同结束日期 <FontAwesomeIcon icon={getSortIconByFieldName('latestContractEndDate')} />
                </th>
                <th className="hand" onClick={sort('contactNumber')}>
                  联系电话 <FontAwesomeIcon icon={getSortIconByFieldName('contactNumber')} />
                </th>
                <th className="hand" onClick={sort('parentsName')}>
                  家长姓名 <FontAwesomeIcon icon={getSortIconByFieldName('parentsName')} />
                </th>
                <th>
                  学校 <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  小区 <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {studentList.map((student, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/student/${student.id}`} color="link" size="sm">
                      {student.id}
                    </Button>
                  </td>
                  <td>{student.name}</td>
                  <td>{student.gender}</td>
                  <td>{student.birthday ? <TextFormat type="date" value={student.birthday} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{student.registerDate ? <TextFormat type="date" value={student.registerDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{student.updateDate ? <TextFormat type="date" value={student.updateDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {student.latestContractEndDate ? (
                      <TextFormat type="date" value={student.latestContractEndDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{student.contactNumber}</td>
                  <td>{student.parentsName}</td>
                  <td>{student.school ? <Link to={`/school/${student.school.id}`}>{student.school.name}</Link> : ''}</td>
                  <td>{student.community ? <Link to={`/community/${student.community.id}`}>{student.community.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/student/${student.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">查看</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/student/${student.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/student/${student.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
          <EmptyStateMessage message="未找到学生" loading={loading} />
        )}
      </div>
      {totalItems ? (
        <div className={studentList && studentList.length > 0 ? '' : 'd-none'}>
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

export default Student;
