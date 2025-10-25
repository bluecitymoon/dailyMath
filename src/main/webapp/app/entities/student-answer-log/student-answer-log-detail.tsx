import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student-answer-log.reducer';

export const StudentAnswerLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentAnswerLogEntity = useAppSelector(state => state.studentAnswerLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentAnswerLogDetailsHeading">Student Answer Log</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{studentAnswerLogEntity.id}</dd>
          <dt>
            <span id="studentId">Student Id</span>
          </dt>
          <dd>{studentAnswerLogEntity.studentId}</dd>
          <dt>
            <span id="questionId">Question Id</span>
          </dt>
          <dd>{studentAnswerLogEntity.questionId}</dd>
          <dt>
            <span id="answer">Answer</span>
          </dt>
          <dd>{studentAnswerLogEntity.answer}</dd>
          <dt>
            <span id="correct">Correct</span>
          </dt>
          <dd>{studentAnswerLogEntity.correct}</dd>
          <dt>
            <span id="createDate">Create Date</span>
          </dt>
          <dd>
            {studentAnswerLogEntity.createDate ? (
              <TextFormat value={studentAnswerLogEntity.createDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="winPoints">Win Points</span>
          </dt>
          <dd>{studentAnswerLogEntity.winPoints}</dd>
        </dl>
        <Button tag={Link} to="/student-answer-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student-answer-log/${studentAnswerLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentAnswerLogDetail;
