import React, { useEffect } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faQuestion } from '@fortawesome/free-solid-svg-icons';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question.reducer';

interface QuestionDetailModalProps {
  isOpen: boolean;
  toggle: () => void;
  questionId: number | null;
}

export const QuestionDetailModal: React.FC<QuestionDetailModalProps> = ({ isOpen, toggle, questionId }) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (isOpen && questionId) {
      dispatch(getEntity(questionId));
    }
  }, [isOpen, questionId]);

  const questionEntity = useAppSelector(state => state.question.entity);
  const loading = useAppSelector(state => state.question.loading);

  return (
    <Modal isOpen={isOpen} toggle={toggle} size="lg" backdrop="static">
      <ModalHeader toggle={toggle}>
        <FontAwesomeIcon icon={faQuestion} className="me-2" />
        题目详情
      </ModalHeader>
      <ModalBody>
        {loading ? (
          <div className="text-center">
            <div className="spinner-border" role="status">
              <span className="visually-hidden">加载中...</span>
            </div>
          </div>
        ) : (
          <Row>
            <Col md="12">
              <dl className="jh-entity-details">
                <dt>
                  <span id="id">ID</span>
                </dt>
                <dd>{questionEntity.id}</dd>
                <dt>
                  <span id="points">奖励积分</span>
                </dt>
                <dd>{questionEntity.points || '未设置'}</dd>
                <dt>
                  <span id="level">难度级别</span>
                </dt>
                <dd>{questionEntity.level || '未设置'}</dd>
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
                <dd>{questionEntity.solutionExternalLink || '未设置'}</dd>
                <dt>
                  <span id="createDate">创建时间</span>
                </dt>
                <dd>
                  {questionEntity.createDate ? (
                    <TextFormat value={questionEntity.createDate} type="date" format={APP_DATE_FORMAT} />
                  ) : (
                    '未设置'
                  )}
                </dd>
                <dt>
                  <span id="updateDate">更新时间</span>
                </dt>
                <dd>
                  {questionEntity.updateDate ? (
                    <TextFormat value={questionEntity.updateDate} type="date" format={APP_DATE_FORMAT} />
                  ) : (
                    '未设置'
                  )}
                </dd>
                <dt>
                  <span id="createBy">创建人</span>
                </dt>
                <dd>{questionEntity.createBy || '未设置'}</dd>
                <dt>
                  <span id="createByUserId">创建人用户ID</span>
                </dt>
                <dd>{questionEntity.createByUserId || '未设置'}</dd>
                <dt>题目分类</dt>
                <dd>{questionEntity.questionCategory ? questionEntity.questionCategory.name : '未设置'}</dd>
                <dt>类型</dt>
                <dd>{questionEntity.type ? questionEntity.type.name : '未设置'}</dd>
                <dt>年级</dt>
                <dd>{questionEntity.grade ? questionEntity.grade.name : '未设置'}</dd>
              </dl>
            </Col>
          </Row>
        )}
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={toggle}>
          <FontAwesomeIcon icon={faTimes} className="me-1" />
          关闭
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default QuestionDetailModal;
