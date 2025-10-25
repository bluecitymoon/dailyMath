import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './school.reducer';

export const SchoolDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const schoolEntity = useAppSelector(state => state.school.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="schoolDetailsHeading">School</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{schoolEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{schoolEntity.name}</dd>
          <dt>
            <span id="registeredStudentsCount">Registered Students Count</span>
          </dt>
          <dd>{schoolEntity.registeredStudentsCount}</dd>
          <dt>
            <span id="pinyin">Pinyin</span>
          </dt>
          <dd>{schoolEntity.pinyin}</dd>
          <dt>Distinct</dt>
          <dd>{schoolEntity.distinct ? schoolEntity.distinct.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/school" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/school/${schoolEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SchoolDetail;
