import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question-base-group.reducer';

export const QuestionBaseGroupDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionBaseGroupEntity = useAppSelector(state => state.questionBaseGroup.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionBaseGroupDetailsHeading">Question Base Group</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{questionBaseGroupEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{questionBaseGroupEntity.title}</dd>
          <dt>
            <span id="questionIds">Question Ids</span>
          </dt>
          <dd>{questionBaseGroupEntity.questionIds}</dd>
        </dl>
        <Button tag={Link} to="/question-base-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question-base-group/${questionBaseGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionBaseGroupDetail;
