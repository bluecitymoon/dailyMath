import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './distinct.reducer';

export const DistinctDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const distinctEntity = useAppSelector(state => state.distinct.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="distinctDetailsHeading">区域详情</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{distinctEntity.id}</dd>
          <dt>
            <span id="name">名称</span>
          </dt>
          <dd>{distinctEntity.name}</dd>
          <dt>
            <span id="pinyin">拼音</span>
          </dt>
          <dd>{distinctEntity.pinyin}</dd>
        </dl>
        <Button tag={Link} to="/distinct" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/distinct/${distinctEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DistinctDetail;
