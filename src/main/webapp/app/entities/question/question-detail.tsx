import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question.reducer';

export const QuestionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionEntity = useAppSelector(state => state.question.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionDetailsHeading">题目</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{questionEntity.id}</dd>
          <dt>
            <span id="points">奖励积分</span>
          </dt>
          <dd>{questionEntity.points}</dd>
          <dt>
            <span id="level">难度级别</span>
          </dt>
          <dd>{questionEntity.level}</dd>
          <dt>
            <span id="description">题目描述</span>
          </dt>
          <dd>
            <div
              dangerouslySetInnerHTML={{ __html: questionEntity.description || '' }}
              style={{
                border: '1px solid #ddd',
                padding: '15px',
                borderRadius: '5px',
                backgroundColor: '#f9f9f9',
                minHeight: '50px',
              }}
            />
          </dd>
          <dt>
            <span id="solution">解题思路</span>
          </dt>
          <dd>
            <div
              dangerouslySetInnerHTML={{ __html: questionEntity.solution || '' }}
              style={{
                border: '1px solid #ddd',
                padding: '15px',
                borderRadius: '5px',
                backgroundColor: '#f9f9f9',
                minHeight: '50px',
              }}
            />
          </dd>
          <dt>
            <span id="solutionExternalLink">解题思路链接</span>
          </dt>
          <dd>{questionEntity.solutionExternalLink}</dd>
          <dt>
            <span id="createDate">创建时间</span>
          </dt>
          <dd>
            {questionEntity.createDate ? <TextFormat value={questionEntity.createDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updateDate">更新时间</span>
          </dt>
          <dd>
            {questionEntity.updateDate ? <TextFormat value={questionEntity.updateDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="createBy">创建人</span>
          </dt>
          <dd>{questionEntity.createBy}</dd>
          <dt>
            <span id="createByUserId">创建人用户ID</span>
          </dt>
          <dd>{questionEntity.createByUserId}</dd>
          <dt>题目分类</dt>
          <dd>{questionEntity.questionCategory ? questionEntity.questionCategory.name : ''}</dd>
          <dt>类型</dt>
          <dd>{questionEntity.type ? questionEntity.type.name : ''}</dd>
          <dt>年级</dt>
          <dd>{questionEntity.grade ? questionEntity.grade.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/question" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">返回</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question/${questionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionDetail;
