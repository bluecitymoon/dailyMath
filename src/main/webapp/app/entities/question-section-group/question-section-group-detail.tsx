import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question-section-group.reducer';

export const QuestionSectionGroupDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionSectionGroupEntity = useAppSelector(state => state.questionSectionGroup.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionSectionGroupDetailsHeading">Question Section Group</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{questionSectionGroupEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{questionSectionGroupEntity.title}</dd>
          <dt>
            <span id="baseGroupIds">Base Group Ids</span>
          </dt>
          <dd>{questionSectionGroupEntity.baseGroupIds}</dd>
          <dt>Grade</dt>
          <dd>{questionSectionGroupEntity.grade ? questionSectionGroupEntity.grade.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/question-section-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question-section-group/${questionSectionGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionSectionGroupDetail;
