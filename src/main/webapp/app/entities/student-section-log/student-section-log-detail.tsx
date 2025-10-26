import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student-section-log.reducer';

export const StudentSectionLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentSectionLogEntity = useAppSelector(state => state.studentSectionLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentSectionLogDetailsHeading">Student Section Log</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{studentSectionLogEntity.id}</dd>
          <dt>
            <span id="studentId">Student Id</span>
          </dt>
          <dd>{studentSectionLogEntity.studentId}</dd>
          <dt>
            <span id="sectionId">Section Id</span>
          </dt>
          <dd>{studentSectionLogEntity.sectionId}</dd>
          <dt>
            <span id="totalCount">Total Count</span>
          </dt>
          <dd>{studentSectionLogEntity.totalCount}</dd>
          <dt>
            <span id="finishedCount">Finished Count</span>
          </dt>
          <dd>{studentSectionLogEntity.finishedCount}</dd>
          <dt>
            <span id="correctRate">Correct Rate</span>
          </dt>
          <dd>{studentSectionLogEntity.correctRate}</dd>
          <dt>
            <span id="createDate">Create Date</span>
          </dt>
          <dd>
            {studentSectionLogEntity.createDate ? (
              <TextFormat value={studentSectionLogEntity.createDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updateDate">Update Date</span>
          </dt>
          <dd>
            {studentSectionLogEntity.updateDate ? (
              <TextFormat value={studentSectionLogEntity.updateDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/student-section-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student-section-log/${studentSectionLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentSectionLogDetail;
