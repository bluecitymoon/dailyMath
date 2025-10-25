import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp, faUser } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './student-answer-log.reducer';
import StudentDetailModal from '../student/student-detail-modal';
import QuestionDetailModal from '../question/question-detail-modal';

export const StudentAnswerLog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  // Modal state
  const [isStudentModalOpen, setIsStudentModalOpen] = useState(false);
  const [selectedStudentId, setSelectedStudentId] = useState<number | null>(null);
  const [isQuestionModalOpen, setIsQuestionModalOpen] = useState(false);
  const [selectedQuestionId, setSelectedQuestionId] = useState<number | null>(null);

  const studentAnswerLogList = useAppSelector(state => state.studentAnswerLog.entities);
  const loading = useAppSelector(state => state.studentAnswerLog.loading);
  const totalItems = useAppSelector(state => state.studentAnswerLog.totalItems);

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

  const handleStudentClick = (studentId: number) => {
    setSelectedStudentId(studentId);
    setIsStudentModalOpen(true);
  };

  const toggleStudentModal = () => {
    setIsStudentModalOpen(!isStudentModalOpen);
    if (isStudentModalOpen) {
      setSelectedStudentId(null);
    }
  };

  const handleQuestionClick = (questionId: number) => {
    setSelectedQuestionId(questionId);
    setIsQuestionModalOpen(true);
  };

  const toggleQuestionModal = () => {
    setIsQuestionModalOpen(!isQuestionModalOpen);
    if (isQuestionModalOpen) {
      setSelectedQuestionId(null);
    }
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
      <h2 id="student-answer-log-heading" data-cy="StudentAnswerLogHeading">
        Student Answer Logs
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {studentAnswerLogList && studentAnswerLogList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('studentId')}>
                  Student Id <FontAwesomeIcon icon={getSortIconByFieldName('studentId')} />
                </th>
                <th className="hand" onClick={sort('questionId')}>
                  Question Id <FontAwesomeIcon icon={getSortIconByFieldName('questionId')} />
                </th>
                <th className="hand" onClick={sort('answer')}>
                  Answer <FontAwesomeIcon icon={getSortIconByFieldName('answer')} />
                </th>
                <th className="hand" onClick={sort('correct')}>
                  Correct <FontAwesomeIcon icon={getSortIconByFieldName('correct')} />
                </th>
                <th className="hand" onClick={sort('createDate')}>
                  Create Date <FontAwesomeIcon icon={getSortIconByFieldName('createDate')} />
                </th>
                <th className="hand" onClick={sort('winPoints')}>
                  Win Points <FontAwesomeIcon icon={getSortIconByFieldName('winPoints')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {studentAnswerLogList.map((studentAnswerLog, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/student-answer-log/${studentAnswerLog.id}`} color="link" size="sm">
                      {studentAnswerLog.id}
                    </Button>
                  </td>
                  <td>
                    <Button
                      color="link"
                      size="sm"
                      onClick={() => handleStudentClick(studentAnswerLog.studentId)}
                      className="p-0 text-decoration-none"
                      style={{ color: '#007bff', cursor: 'pointer' }}
                    >
                      <FontAwesomeIcon icon={faUser} className="me-1" />
                      {studentAnswerLog.studentId}
                    </Button>
                  </td>
                  <td>
                    <Button
                      color="link"
                      size="sm"
                      onClick={() => handleQuestionClick(studentAnswerLog.questionId)}
                      className="p-0 text-decoration-none"
                      style={{ color: '#007bff', cursor: 'pointer' }}
                    >
                      <FontAwesomeIcon icon="question" className="me-1" />
                      {studentAnswerLog.questionId}
                    </Button>
                  </td>
                  <td>{studentAnswerLog.answer}</td>
                  <td>{studentAnswerLog.correct}</td>
                  <td>
                    {studentAnswerLog.createDate ? (
                      <TextFormat type="date" value={studentAnswerLog.createDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{studentAnswerLog.winPoints}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/student-answer-log/${studentAnswerLog.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">查看</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Student Answer Logs found</div>
        )}
      </div>
      {totalItems ? (
        <div className={studentAnswerLogList && studentAnswerLogList.length > 0 ? '' : 'd-none'}>
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

      {/* Student Detail Modal */}
      <StudentDetailModal isOpen={isStudentModalOpen} toggle={toggleStudentModal} studentId={selectedStudentId} />

      {/* Question Detail Modal */}
      <QuestionDetailModal isOpen={isQuestionModalOpen} toggle={toggleQuestionModal} questionId={selectedQuestionId} />
    </div>
  );
};

export default StudentAnswerLog;
