import React, { useEffect } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student.reducer';

interface StudentDetailModalProps {
  isOpen: boolean;
  toggle: () => void;
  studentId: number | null;
}

export const StudentDetailModal: React.FC<StudentDetailModalProps> = ({ isOpen, toggle, studentId }) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (isOpen && studentId) {
      dispatch(getEntity(studentId));
    }
  }, [isOpen, studentId]);

  const studentEntity = useAppSelector(state => state.student.entity);
  const loading = useAppSelector(state => state.student.loading);

  return (
    <Modal isOpen={isOpen} toggle={toggle} size="lg" backdrop="static">
      <ModalHeader toggle={toggle}>
        <FontAwesomeIcon icon="user" className="me-2" />
        学生详情
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
                <dd>
                  {studentEntity.birthday ? <TextFormat value={studentEntity.birthday} type="date" format={APP_DATE_FORMAT} /> : '未设置'}
                </dd>
                <dt>
                  <span id="registerDate">注册日期</span>
                </dt>
                <dd>
                  {studentEntity.registerDate ? (
                    <TextFormat value={studentEntity.registerDate} type="date" format={APP_DATE_FORMAT} />
                  ) : (
                    '未设置'
                  )}
                </dd>
                <dt>
                  <span id="updateDate">更新日期</span>
                </dt>
                <dd>
                  {studentEntity.updateDate ? (
                    <TextFormat value={studentEntity.updateDate} type="date" format={APP_DATE_FORMAT} />
                  ) : (
                    '未设置'
                  )}
                </dd>
                <dt>
                  <span id="latestContractEndDate">最新合同结束日期</span>
                </dt>
                <dd>
                  {studentEntity.latestContractEndDate ? (
                    <TextFormat value={studentEntity.latestContractEndDate} type="date" format={APP_DATE_FORMAT} />
                  ) : (
                    '未设置'
                  )}
                </dd>
                <dt>
                  <span id="contactNumber">联系电话</span>
                </dt>
                <dd>{studentEntity.contactNumber || '未设置'}</dd>
                <dt>
                  <span id="parentsName">家长姓名</span>
                </dt>
                <dd>{studentEntity.parentsName || '未设置'}</dd>
                <dt>
                  <span id="wechatUserId">微信用户ID</span>
                </dt>
                <dd>{studentEntity.wechatUserId || '未设置'}</dd>
                <dt>
                  <span id="wechatNickname">微信昵称</span>
                </dt>
                <dd>{studentEntity.wechatNickname || '未设置'}</dd>
                <dt>学校</dt>
                <dd>{studentEntity.school ? studentEntity.school.name : '未设置'}</dd>
                <dt>小区</dt>
                <dd>{studentEntity.community ? studentEntity.community.name : '未设置'}</dd>
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

export default StudentDetailModal;
