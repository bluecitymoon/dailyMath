import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question-type.reducer';

export const QuestionTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionTypeEntity = useAppSelector(state => state.questionType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionTypeDetailsHeading">题目类型</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{questionTypeEntity.id}</dd>
          <dt>
            <span id="name">名称</span>
          </dt>
          <dd>{questionTypeEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/question-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question-type/${questionTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionTypeDetail;
