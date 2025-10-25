import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student.reducer';

export const StudentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentEntity = useAppSelector(state => state.student.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentDetailsHeading">Student</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{studentEntity.id}</dd>
          <dt>
            <span id="name">姓名</span>
          </dt>
          <dd>{studentEntity.name}</dd>
          <dt>
            <span id="gender">性别</span>
          </dt>
          <dd>{studentEntity.gender}</dd>
          <dt>
            <span id="birthday">生日</span>
          </dt>
          <dd>{studentEntity.birthday ? <TextFormat value={studentEntity.birthday} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="registerDate">注册日期</span>
          </dt>
          <dd>
            {studentEntity.registerDate ? <TextFormat value={studentEntity.registerDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updateDate">更新日期</span>
          </dt>
          <dd>{studentEntity.updateDate ? <TextFormat value={studentEntity.updateDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="latestContractEndDate">最新合同结束日期</span>
          </dt>
          <dd>
            {studentEntity.latestContractEndDate ? (
              <TextFormat value={studentEntity.latestContractEndDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="contactNumber">联系电话</span>
          </dt>
          <dd>{studentEntity.contactNumber}</dd>
          <dt>
            <span id="parentsName">家长姓名</span>
          </dt>
          <dd>{studentEntity.parentsName}</dd>
          <dt>学校</dt>
          <dd>{studentEntity.school ? studentEntity.school.name : ''}</dd>
          <dt>小区</dt>
          <dd>{studentEntity.community ? studentEntity.community.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/student" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student/${studentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentDetail;
