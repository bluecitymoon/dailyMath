import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Row, Col } from 'reactstrap';
import { JhiItemCount, JhiPagination, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './question-base-group.reducer';
import { getEntities as getStudentGrades } from 'app/entities/student-grade/student-grade.reducer';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { IStudentGrade } from 'app/shared/model/student-grade.model';
import { IQuestion } from 'app/shared/model/question.model';
import EmptyStateMessage from 'app/shared/components/empty-state-message';

export const QuestionBaseGroup = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const questionBaseGroupList = useAppSelector(state => state.questionBaseGroup.entities);
  const loading = useAppSelector(state => state.questionBaseGroup.loading);
  const totalItems = useAppSelector(state => state.questionBaseGroup.totalItems);

  // 获取年级列表
  const studentGradeList = useAppSelector(state => state.studentGrade.entities);
  const studentGradeLoading = useAppSelector(state => state.studentGrade.loading);

  // 获取所有题目数据（用于过滤）
  const allQuestions = useAppSelector(state => state.question.entities);
  const questionLoading = useAppSelector(state => state.question.loading);

  // 过滤状态
  const [selectedGradeId, setSelectedGradeId] = useState<number | null>(null);
  const [filteredGroups, setFilteredGroups] = useState(questionBaseGroupList);

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

  // 加载年级数据
  useEffect(() => {
    if (studentGradeList.length === 0 && !studentGradeLoading) {
      dispatch(getStudentGrades({ page: 0, size: 1000, sort: 'id,asc' }));
    }
  }, [dispatch, studentGradeList.length, studentGradeLoading]);

  // 加载所有题目数据（用于过滤）
  useEffect(() => {
    if (allQuestions.length === 0 && !questionLoading) {
      dispatch(getQuestions({ page: 0, size: 10000, sort: 'id,asc' }));
    }
  }, [dispatch, allQuestions.length, questionLoading]);

  // 根据年级过滤题目基础分组
  useEffect(() => {
    if (!selectedGradeId) {
      setFilteredGroups(questionBaseGroupList);
      return;
    }

    const filtered = questionBaseGroupList.filter(group => {
      if (!group.questionIds) return false;

      try {
        const questionIds = JSON.parse(group.questionIds);
        if (!Array.isArray(questionIds)) return false;

        // 检查是否有任何题目属于选中的年级
        return questionIds.some(questionItem => {
          const questionId = typeof questionItem === 'object' ? questionItem.id : questionItem;
          const question = allQuestions.find(q => q.id === questionId);
          return question && question.grade && question.grade.id === selectedGradeId;
        });
      } catch (error) {
        return false;
      }
    });

    setFilteredGroups(filtered);
  }, [selectedGradeId, questionBaseGroupList, allQuestions]);

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

  const handleGradeChange = (gradeId: number | null) => {
    setSelectedGradeId(gradeId);
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
      <h2 id="question-base-group-heading" data-cy="QuestionBaseGroupHeading">
        题目基础分组
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> 刷新列表
          </Button>
          <Link
            to="/question-base-group/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; 创建新题目基础分组
          </Link>
        </div>
      </h2>

      {/* 年级过滤区域 */}
      <Row className="mb-3">
        <Col md="4">
          <label htmlFor="grade-filter" className="form-label">
            <FontAwesomeIcon icon="filter" /> 按年级过滤
          </label>
          <select
            id="grade-filter"
            className="form-select"
            value={selectedGradeId || ''}
            onChange={e => handleGradeChange(e.target.value ? Number(e.target.value) : null)}
            disabled={studentGradeLoading}
          >
            <option value="">所有年级</option>
            {studentGradeList.map((grade: IStudentGrade) => (
              <option key={grade.id} value={grade.id}>
                {grade.name}
              </option>
            ))}
          </select>
        </Col>
        <Col md="8" className="d-flex align-items-end">
          {selectedGradeId && (
            <div className="text-muted">
              已选择年级: {studentGradeList.find((g: IStudentGrade) => g.id === selectedGradeId)?.name}
              <Button color="link" size="sm" onClick={() => handleGradeChange(null)} className="ms-2">
                清除过滤
              </Button>
            </div>
          )}
        </Col>
      </Row>

      <div className="table-responsive">
        {filteredGroups && filteredGroups.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('title')}>
                  标题 <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                </th>
                <th className="hand" onClick={sort('questionIds')}>
                  题目ID <FontAwesomeIcon icon={getSortIconByFieldName('questionIds')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filteredGroups.map((questionBaseGroup, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/question-base-group/${questionBaseGroup.id}`} color="link" size="sm">
                      {questionBaseGroup.id}
                    </Button>
                  </td>
                  <td>{questionBaseGroup.title}</td>
                  <td>{questionBaseGroup.questionIds}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/question-base-group/${questionBaseGroup.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">查看</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/question-base-group/${questionBaseGroup.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/question-base-group/${questionBaseGroup.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
          <EmptyStateMessage message={selectedGradeId ? '未找到符合条件的题目基础分组' : '未找到题目基础分组'} loading={loading} />
        )}
      </div>
      {selectedGradeId ? (
        // 过滤模式下不显示分页，显示过滤结果统计
        <div className="text-center text-muted mt-3">显示 {filteredGroups.length} 个符合条件的题目基础分组</div>
      ) : totalItems ? (
        // 正常模式下显示分页
        <div className={questionBaseGroupList && questionBaseGroupList.length > 0 ? '' : 'd-none'}>
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

export default QuestionBaseGroup;
