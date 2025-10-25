import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student-grade.reducer';

export const StudentGradeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentGradeEntity = useAppSelector(state => state.studentGrade.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentGradeDetailsHeading">学生年级详情</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{studentGradeEntity.id}</dd>
          <dt>
            <span id="name">名称</span>
          </dt>
          <dd>{studentGradeEntity.name}</dd>
          <dt>
            <span id="index">索引</span>
          </dt>
          <dd>{studentGradeEntity.index}</dd>
          <dt>
            <span id="term">学期</span>
          </dt>
          <dd>{studentGradeEntity.term}</dd>
        </dl>
        <Button tag={Link} to="/student-grade" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student-grade/${studentGradeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentGradeDetail;
