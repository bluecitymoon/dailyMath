import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question-option.reducer';

export const QuestionOptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionOptionEntity = useAppSelector(state => state.questionOption.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionOptionDetailsHeading">题目选项</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{questionOptionEntity.id}</dd>
          <dt>
            <span id="name">名称</span>
          </dt>
          <dd>{questionOptionEntity.name}</dd>
          <dt>
            <span id="type">类型</span>
          </dt>
          <dd>{questionOptionEntity.type}</dd>
          <dt>
            <span id="imageUrl">图片地址</span>
          </dt>
          <dd>{questionOptionEntity.imageUrl}</dd>
          <dt>题目</dt>
          <dd>{questionOptionEntity.question ? questionOptionEntity.question.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/question-option" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question-option/${questionOptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionOptionDetail;
