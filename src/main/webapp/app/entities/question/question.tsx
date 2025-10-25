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

import { getEntities } from './question.reducer';
import EmptyStateMessage from 'app/shared/components/empty-state-message';

export const Question = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id', DESC), pageLocation.search),
  );

  const questionList = useAppSelector(state => state.question.entities);
  const loading = useAppSelector(state => state.question.loading);
  const totalItems = useAppSelector(state => state.question.totalItems);

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
      <h2 id="question-heading" data-cy="QuestionHeading">
        题目列表
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> 刷新列表
          </Button>
          <Link to="/question/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; 创建新 Question
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {questionList && questionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('points')}>
                  积分 <FontAwesomeIcon icon={getSortIconByFieldName('points')} />
                </th>
                <th className="hand" onClick={sort('level')}>
                  难度级别 <FontAwesomeIcon icon={getSortIconByFieldName('level')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  描述 <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('solution')}>
                  解题思路 <FontAwesomeIcon icon={getSortIconByFieldName('solution')} />
                </th>
                <th className="hand" onClick={sort('solutionExternalLink')}>
                  解题思路链接 <FontAwesomeIcon icon={getSortIconByFieldName('solutionExternalLink')} />
                </th>
                <th className="hand" onClick={sort('createDate')}>
                  创建时间 <FontAwesomeIcon icon={getSortIconByFieldName('createDate')} />
                </th>
                <th className="hand" onClick={sort('updateDate')}>
                  更新时间 <FontAwesomeIcon icon={getSortIconByFieldName('updateDate')} />
                </th>
                <th className="hand" onClick={sort('createBy')}>
                  创建人 <FontAwesomeIcon icon={getSortIconByFieldName('createBy')} />
                </th>
                <th className="hand" onClick={sort('createByUserId')}>
                  创建人用户ID <FontAwesomeIcon icon={getSortIconByFieldName('createByUserId')} />
                </th>
                <th>
                  题目分类 <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  类型 <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  年级 <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {questionList.map((question, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/question/${question.id}`} color="link" size="sm">
                      {question.id}
                    </Button>
                  </td>
                  <td>{question.points}</td>
                  <td>{question.level}</td>
                  <td>
                    <div dangerouslySetInnerHTML={{ __html: question.description }} />
                  </td>
                  <td>
                    <div dangerouslySetInnerHTML={{ __html: question.solution }} />
                  </td>
                  <td>{question.solutionExternalLink}</td>
                  <td>{question.createDate ? <TextFormat type="date" value={question.createDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{question.updateDate ? <TextFormat type="date" value={question.updateDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{question.createBy}</td>
                  <td>{question.createByUserId}</td>
                  <td>
                    {question.questionCategory ? (
                      <Link to={`/question-category/${question.questionCategory.id}`}>{question.questionCategory.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{question.type ? <Link to={`/question-type/${question.type.id}`}>{question.type.name}</Link> : ''}</td>
                  <td>{question.grade ? <Link to={`/student-grade/${question.grade.id}`}>{question.grade.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/question/${question.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">查看</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/question/${question.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/question/${question.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
          <EmptyStateMessage message="未找到题目" loading={loading} />
        )}
      </div>
      {totalItems ? (
        <div className={questionList && questionList.length > 0 ? '' : 'd-none'}>
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

export default Question;
