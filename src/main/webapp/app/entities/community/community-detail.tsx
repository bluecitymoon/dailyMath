import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './community.reducer';

export const CommunityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const communityEntity = useAppSelector(state => state.community.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="communityDetailsHeading">小区详情</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{communityEntity.id}</dd>
          <dt>
            <span id="name">名称</span>
          </dt>
          <dd>{communityEntity.name}</dd>
          <dt>
            <span id="lat">纬度</span>
          </dt>
          <dd>{communityEntity.lat}</dd>
          <dt>
            <span id="lon">经度</span>
          </dt>
          <dd>{communityEntity.lon}</dd>
          <dt>
            <span id="studentsCount">学生数</span>
          </dt>
          <dd>{communityEntity.studentsCount}</dd>
          <dt>
            <span id="createDate">创建日期</span>
          </dt>
          <dd>
            {communityEntity.createDate ? <TextFormat value={communityEntity.createDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>区域</dt>
          <dd>{communityEntity.distinct ? communityEntity.distinct.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/community" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/community/${communityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommunityDetail;
